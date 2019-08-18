package com.judge.app.fragments.judge

import android.graphics.Color
import android.widget.Toast
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.airbnb.mvrx.fragmentViewModel
import com.judge.app.core.BaseFragment
import com.judge.app.core.MvRxViewModel
import com.judge.app.core.simpleController
import com.judge.data.bean.Recommend
import com.judge.data.bean.RecommendBean
import com.judge.data.repository.JudgeRepository
import com.judge.recommendItem
import com.judge.utils.LogUtils
import com.vondear.rxtool.view.RxToast
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.collections.forEachWithIndex
import org.jetbrains.anko.support.v4.runOnUiThread

/**
 * @author: jaffa
 * @date: 2019/8/11
 * 推荐
 */
data class RecommendState(
    val recommendItems: List<Recommend>? = emptyList(),
    val isLoading: Boolean = false
) : MvRxState

class RecommendViewModel(initialState: RecommendState) : MvRxViewModel<RecommendState>(initialState) {

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
        override fun create(viewModelContext: ViewModelContext, state: RecommendState): RecommendViewModel? {
            return RecommendViewModel(state)
        }
    }
}

class RecommendFragment : BaseFragment() {

    private val viewModel: RecommendViewModel by fragmentViewModel()

    override fun epoxyController() = simpleController(viewModel) { state ->

        runOnUiThread {
            if (state.isLoading) {
                loadingDialog.show()
            } else {
                loadingDialog.dismiss()
            }

        }

        state.recommendItems?.forEachWithIndex { idnex, item ->

            recommendItem {
                id(item.tid + idnex)
                recommend(item)
            }
        }

    }

    override fun initView() {
        recyclerView.setBackgroundColor(Color.parseColor("#f6f5fa"))
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