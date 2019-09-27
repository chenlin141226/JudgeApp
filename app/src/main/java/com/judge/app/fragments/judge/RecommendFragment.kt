package com.judge.app.fragments.judge

import android.graphics.Color
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.airbnb.mvrx.fragmentViewModel
import com.judge.R
import com.judge.app.core.BaseFragment
import com.judge.app.core.MvRxViewModel
import com.judge.app.core.simpleController
import com.judge.data.bean.Detail
import com.judge.data.bean.Recommend
import com.judge.data.repository.JudgeRepository
import com.judge.recommendItem
import com.judge.utils.LogUtils
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.collections.forEachWithIndex

/**
 * @author: jaffa
 * @date: 2019/8/11
 * 推荐
 */
data class RecommendState(
    val recommendItems: List<Recommend>? = emptyList(),
    val isLoading: Boolean = false
) : MvRxState

class RecommendViewModel(initialState: RecommendState) :
    MvRxViewModel<RecommendState>(initialState) {

    init {
        fetchRecommend()
    }

    fun fetchRecommend() = withState { state ->
        if (state.isLoading) return@withState

        JudgeRepository.getRecommend().subscribeOn(Schedulers.io())
            .doOnSubscribe { setState { copy(isLoading = true) } }
            .doOnError { it.message?.let { it1 -> LogUtils.e(it1) } }
            .doFinally { setState { copy(isLoading = false) } }
            .execute { copy(recommendItems = it()?.Variables?.data ?: emptyList()) }
    }

    fun removeRecommend() {
    }


    companion object : MvRxViewModelFactory<RecommendViewModel, RecommendState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: RecommendState
        ): RecommendViewModel? {
            return RecommendViewModel(state)
        }
    }
}

class RecommendFragment : BaseFragment() {

    private val viewModel: RecommendViewModel by fragmentViewModel()

    val args = Detail()

    override fun epoxyController() = simpleController(viewModel) { state ->

        state.recommendItems?.forEachWithIndex { idnex, item ->

            recommendItem {
                id(item.tid + idnex)
                recommend(item)


                onParentClick { _ ->
                    args.tid = item.tid
                     navigateTo(R.id.action_judgeFragment_to_newsDetailFragment,args)
                }
            }
        }

    }

    override fun initView() {
        recyclerView.setBackgroundColor(Color.parseColor("#f6f5fa"))

        viewModel.selectSubscribe(RecommendState::isLoading) {
            if (it) {
                loadingDialog.show()
            } else {
                loadingDialog.dismiss()
            }
        }

        refreshLayout.apply {
            setEnableAutoLoadMore(true)
            setEnableRefresh(true)
            setEnableLoadMore(true)
            setOnRefreshListener {
                viewModel.fetchRecommend()
                it.finishRefresh(1000)
            }

            setOnLoadMoreListener {

                it.finishLoadMore(1000)
            }
        }
    }

}