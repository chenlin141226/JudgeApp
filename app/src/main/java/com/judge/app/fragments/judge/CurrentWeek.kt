package com.judge.app.fragments.judge

import com.airbnb.mvrx.*
import com.judge.app.core.BaseFragment
import com.judge.app.core.MvRxViewModel
import com.judge.app.core.simpleController
import com.judge.data.bean.Data
import com.judge.data.repository.JudgeRepository
import com.judge.todayItem
import com.judge.utils.LogUtils
import com.judge.views.loadingView
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.collections.forEachWithIndex

/**
 * @author: jaffa
 * @date: 2019/8/11
 */
data class WeekState(
    val information: List<Data>? = emptyList(),
    val isLoading: Boolean = false
) : MvRxState

class WeekViewModel(initialiState: WeekState) : MvRxViewModel<WeekState>(initialiState) {

    val map = hashMapOf("version" to "4", "module" to "get_diy", "bid" to "98")

    init {
        refreshWeek()
    }

    fun refreshWeek() = withState { state ->

        if (state.isLoading) return@withState

        JudgeRepository.getInformation(map).subscribeOn(Schedulers.io())
            .doOnSubscribe { setState { copy(isLoading = true) } }
            .doOnError {
                it.message?.let { it1 -> LogUtils.e(it1) }
            }
            .doFinally { setState { copy(isLoading = false) } }
            .execute {
                LogUtils.d(it()?.Variables?.data.toString())
                copy(information = it()?.Variables?.data ?: emptyList())
            }
    }

    companion object : MvRxViewModelFactory<WeekViewModel, WeekState> {
        override fun create(viewModelContext: ViewModelContext, state: WeekState): WeekViewModel? {
            return WeekViewModel(state)
        }
    }
}

class CurrentWeek : BaseFragment() {

    private val viewModel: WeekViewModel by fragmentViewModel()

    override fun epoxyController() = simpleController(viewModel) { state ->

            loadingView {
                id("load")
                loading(state.isLoading)
            }

        state.information?.forEachWithIndex { index, item ->
            todayItem {
                id(item.tid + index)
                informationBean(item)
            }
        }
    }

    override fun initView() {
        refreshLayout.apply {
            setEnableAutoLoadMore(true)
            setEnableRefresh(true)
            setEnableLoadMore(true)
            setOnRefreshListener {
                viewModel.refreshWeek()
                it.finishRefresh(1000)
            }

            setOnLoadMoreListener {
                //viewModel.fetchWeek()
                it.finishLoadMore(1000)
            }
        }
    }

}