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
data class MonthState(
    val information: List<Data>? = emptyList(),
    val isLoading: Boolean = false
) : MvRxState

class MonthViewModel(initialiState: MonthState) : MvRxViewModel<MonthState>(initialiState) {

    val map = hashMapOf("version" to "4", "module" to "get_diy", "bid" to "99")

    init {
        refreshMonth()
    }

    fun refreshMonth() = withState { state ->

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

    companion object : MvRxViewModelFactory<MonthViewModel, MonthState> {
        override fun create(viewModelContext: ViewModelContext, state: MonthState): MonthViewModel? {
            return MonthViewModel(state)
        }
    }
}

class CurrentMonth : BaseFragment() {

    private val viewModel: MonthViewModel by fragmentViewModel()

    override fun epoxyController() = simpleController(viewModel) { state ->

        if(state.information == null){
            loadingView {
                id("load")
                loading(true)
            }
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
                viewModel.refreshMonth()
                it.finishRefresh(1000)
            }

            setOnLoadMoreListener {

                it.finishLoadMore(1000)
            }
        }
    }

}