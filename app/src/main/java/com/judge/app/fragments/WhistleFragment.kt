package com.judge.app.fragments

import android.view.View
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.airbnb.mvrx.fragmentViewModel
import com.judge.R
import com.judge.app.core.BaseFragment
import com.judge.app.core.MvRxEpoxyController
import com.judge.app.core.MvRxViewModel
import com.judge.app.core.simpleController
import com.judge.data.WhistleBean
import com.judge.whistleItem
import org.jetbrains.anko.collections.forEachWithIndex
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.util.*


data class WhistleState(
    val whistleItems: List<WhistleBean> = emptyList()
) : MvRxState

class WhistleViewModel(
    initialState: WhistleState
) : MvRxViewModel<WhistleState>(initialState) {
    private val list = LinkedList<WhistleBean>()
    /*init {
        getWhistles()
    }*/

    fun getWhistles() {
        for (i in 1..10) {
            val bean: WhistleBean = when {
                i % 2 == 0 -> WhistleBean("金哨子", "1", 125)
                i % 3 == 0 -> WhistleBean("银哨子", "2", 111)
                else -> WhistleBean("铜哨子", "3", 113)
            }
            list.add(bean)
        }
        setState {
            copy(whistleItems = list)
        }
    }

    companion object : MvRxViewModelFactory<WhistleViewModel, WhistleState> {
        override fun create(viewModelContext: ViewModelContext, state: WhistleState): WhistleViewModel? {
            return WhistleViewModel(state)
        }
    }
}

class WhistleFragment : BaseFragment() {
    private val viewModel: WhistleViewModel by fragmentViewModel()
    override fun epoxyController(): MvRxEpoxyController = simpleController(viewModel) { state ->
        state.whistleItems.forEachWithIndex { index, item ->
            whistleItem {
                id(item.whistleName + index)
                whistleBean(item)
            }
        }
    }

    override fun initData() {
        viewModel.getWhistles()
    }

    override fun setToolBar() {
        super.setToolBar()
        toolbar.visibility = View.VISIBLE
        rightButton.apply {
            visibility = View.VISIBLE
            onClick {
                navigateTo(R.id.action_whistleFragment_to_whistleRulesFragment, null)
            }
        }
    }

}