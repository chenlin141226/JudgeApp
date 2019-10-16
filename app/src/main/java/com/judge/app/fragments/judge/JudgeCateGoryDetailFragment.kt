package com.judge.app.fragments.judge

import android.graphics.Color
import android.util.Log
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
import java.util.function.LongFunction

/**
 * @author: jaffa
 * @date: 2019/8/18
 * 最新
 */
data class NewState(
    val isLoading: Boolean = false,
    val categoryDetails: List<ForumThreadlist> = emptyList(),
    val page: Int = 1,
    val page_total: String = ""
) : MvRxState

class NewViewModel(initialiState: NewState) : MvRxViewModel<NewState>(initialiState) {

    fun fetchDetail(id: String) = withState { state ->
        if (state.isLoading) return@withState
        val map = hashMapOf("page" to "${state.page}", "fid" to id)
        JudgeRepository.getNewCategoryDetail(map).subscribeOn(Schedulers.io())
            .doOnSubscribe { setState { copy(isLoading = true) } }
            .doOnError { it.message.let { it1 -> LogUtils.e(it1!!) } }
            .doFinally { setState { copy(isLoading = false) } }
            .execute { copy(categoryDetails = it()?.Variables?.forum_threadlist ?: emptyList(),
                page_total = it()?.Variables?.page_total.toString()) }
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
    var pageTotal = 1
    override fun epoxyController() = simpleController(viewModel) { state ->
        state.categoryDetails.forEachWithIndex { index, item ->
            pageTotal = state.page_total.toInt()
            judgeCategoryDetailItem {
                id(item.tid)
                viewmodel(item)
                parentOnclick{_ ->
                    args.tid = item.tid
                    navigateTo(R.id.action_judgeDetailFragment_to_newsDetailFragment,args)
                }
            }
        }

    }

    override fun initView() {
        viewModel.fetchDetail(ids)
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
                    if (page<pageTotal) {
                        page++
                        viewModel.loadMoreDetail(ids,page)
                    }
                    it.finishLoadMore(1000)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        page = 1
        viewModel.clearDatail()
    }

}