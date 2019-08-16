package com.judge.app.fragments.judge

import androidx.databinding.BindingAdapter
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.airbnb.mvrx.fragmentViewModel
import com.judge.app.core.BaseFragment
import com.judge.app.core.MvRxViewModel
import com.judge.app.core.simpleController
import com.judge.data.bean.Data
import com.judge.data.repository.JudgeRepository
import com.judge.todayItem
import com.judge.utils.LogUtils
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.collections.forEachWithIndex
import java.text.SimpleDateFormat
import java.util.*
import android.widget.TextView
import android.widget.Toast
import com.vondear.rxtool.view.RxToast


/**
 * @author: jaffa
 * @date: 2019/8/11
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

    override fun epoxyController() = simpleController(viewModel) { state ->
        if (state.isLoading) {
            loadingDialog.show()
        } else {
            loadingDialog.dismiss()
        }

        state.information?.forEachWithIndex { index, item ->
            todayItem {
                id(item.tid + index)
                informationBean(item)
            }
        }
    }

    @BindingAdapter("data")
    fun setData(textView: TextView, data: String) {
        context?.let { RxToast.info(it, data, Toast.LENGTH_SHORT, true).show() }
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