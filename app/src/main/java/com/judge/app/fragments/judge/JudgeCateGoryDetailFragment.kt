package com.judge.app.fragments.judge

import android.graphics.Color
import com.airbnb.mvrx.*
import com.judge.app.core.BaseFragment
import com.judge.app.core.MvRxViewModel
import com.judge.app.core.simpleController
import com.judge.data.bean.ForumThreadlist
import com.judge.data.repository.JudgeRepository
import com.judge.extensions.clear
import com.judge.judgeCategoryDetailItem
import com.judge.utils.LogUtils
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.collections.forEachWithIndex
import com.judge.R
import com.judge.data.bean.Detail

/**
 * @author: jaffa
 * @date: 2019/8/18
 * 最新
 */
data class NewState(
    val isLoading: Boolean = false,
    val categoryDetails: List<ForumThreadlist> = emptyList(),
    val page: Int = 1
) : MvRxState

class NewViewModel(initialiState: NewState) : MvRxViewModel<NewState>(initialiState) {


    fun fetchDetail(id: String) = withState { state ->
        if (state.isLoading) return@withState
        val map = hashMapOf("page" to "${state.page}", "fid" to id)
        JudgeRepository.getNewCategoryDetail(map).subscribeOn(Schedulers.io())
            .doOnSubscribe { setState { copy(isLoading = true) } }
            .doOnError { it.message.let { it1 -> LogUtils.e(it1!!) } }
            .doFinally { setState { copy(isLoading = false) } }
            .execute { copy(categoryDetails = it()?.Variables?.forum_threadlist ?: emptyList()) }
    }


    fun loadMoreDetail(id: String, page: Int) = withState { state ->

        val map = hashMapOf("page" to "$page", "fid" to id)
        JudgeRepository.getNewCategoryDetail(map).subscribeOn(Schedulers.io())
            .doOnSubscribe { setState { copy(isLoading = true) } }
            .doOnError { it.message.let { it1 -> LogUtils.e(it1!!) } }
            .doFinally { setState { copy(isLoading = false) } }
            .execute {
                copy(
                    categoryDetails = categoryDetails.plus(
                        it()?.Variables?.forum_threadlist ?: emptyList()
                    )
                )
            }
    }

    fun clearDatail() = withState { state ->
        state.categoryDetails.clear()
        setState { copy(categoryDetails = categoryDetails) }
    }

    companion object : MvRxViewModelFactory<NewViewModel, NewState> {

        override fun create(viewModelContext: ViewModelContext, state: NewState): NewViewModel? {
            return NewViewModel(state)
        }
    }
}

class JudgeCateGoryDetailFragment(id: String) : BaseFragment() {
    private val viewModel: NewViewModel by fragmentViewModel()
    var ids = id
    var page = 1

    val args = Detail()
    override fun epoxyController() = simpleController(viewModel) { state ->

        state.categoryDetails.forEachWithIndex { index, item ->
            judgeCategoryDetailItem {
                id(item.tid)
                viewmodel(item)
                args.tid = item.tid
                parentOnclick{_ ->

                }
            }
        }

    }

    override fun initView() {
        recyclerView.setBackgroundColor(Color.WHITE)
        withState(viewModel) { state ->
            refreshLayout.apply {
                setEnableAutoLoadMore(true)
                setEnableRefresh(true)
                setEnableLoadMore(true)
                setOnRefreshListener {
                    viewModel.fetchDetail(ids)
                    it.finishRefresh(1000)
                }
                setOnLoadMoreListener {
                    if (page <= 10) {
                        page++
                        viewModel.loadMoreDetail(ids,page)
                    }
                    it.finishLoadMore(1000)
                }
            }
        }
        viewModel.fetchDetail(ids)
    }

    override fun onDestroy() {
        super.onDestroy()
        page = 1
        viewModel.clearDatail()
    }

}