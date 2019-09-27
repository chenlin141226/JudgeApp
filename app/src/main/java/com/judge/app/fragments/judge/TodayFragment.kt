package com.judge.app.fragments.judge

import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.airbnb.mvrx.fragmentViewModel
import com.judge.R
import com.judge.app.core.BaseFragment
import com.judge.app.core.MvRxViewModel
import com.judge.app.core.simpleController
import com.judge.data.bean.Data
import com.judge.data.bean.Detail
import com.judge.data.repository.JudgeRepository
import com.judge.todayItem
import com.judge.utils.LogUtils
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.collections.forEachWithIndex


/**
 * @author: jaffa
 * @date: 2019/8/11
 * 当日
 */
data class TodayState(
    val information: List<Data>? = emptyList(),
    val times: String? = null,
    val isLoading: Boolean = false
) : MvRxState

class TodayViewModel(initialiState: TodayState) : MvRxViewModel<TodayState>(initialiState) {

    val map = hashMapOf("version" to "4", "module" to "get_diy", "bid" to "97")

    init {
        refreshToday()
    }

    fun refreshToday() = withState { state ->

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

//    fun fetchToday() = withState { state ->
//        if (state.isLoading) return@withState
//        JudgeRepository.getInformation(map).subscribeOn(Schedulers.io())
//            .doOnSubscribe { setState { copy(isLoading = true) } }
//            .doOnError {
//                it.message?.let { it1 -> LogUtils.e(it1) }
//            }
//            .doFinally { setState { copy(isLoading = false) } }
//            .execute {
//                LogUtils.d(it()?.Variables?.data.toString())
//                copy(information = information!! + (it()?.Variables?.data ?: emptyList()))
//            }
//    }


    companion object : MvRxViewModelFactory<TodayViewModel, TodayState> {
        override fun create(viewModelContext: ViewModelContext, state: TodayState): TodayViewModel? {
            return TodayViewModel(state)
        }
    }
}

class TodayFragment : BaseFragment() {

    private val viewModel: TodayViewModel by fragmentViewModel()
    val args = Detail()

    override fun epoxyController() = simpleController(viewModel) { state ->

        state.information?.forEachWithIndex { index, item ->
            todayItem {
                id(item.tid + index)
                informationBean(item)
                parentOnClick { _ ->
                    args.tid = item.tid
                    navigateTo(R.id.action_judgeFragment_to_newsDetailFragment,args)
                }
            }
        }
    }

    override fun initView() {
        refreshLayout.apply {
            setEnableAutoLoadMore(true)
            setEnableRefresh(true)
            setEnableLoadMore(true)
            setOnRefreshListener {
                viewModel.refreshToday()
                it.finishRefresh(1000)
            }

            setOnLoadMoreListener {
                //viewModel.fetchToday()
                it.finishLoadMore(1000)
            }
        }
    }

}