package com.judge.app.fragments

import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.airbnb.mvrx.fragmentViewModel
import com.judge.app.core.BaseFragment
import com.judge.app.core.MvRxEpoxyController
import com.judge.app.core.MvRxViewModel
import com.judge.app.core.simpleController
import com.judge.data.MineItemBean
import com.judge.data.WhistleBean


data class WhistleState(
    val whistleItems: List<WhistleBean> = emptyList()
) : MvRxState

class WhistleViewModel(
    initialState: WhistleState
) : MvRxViewModel<WhistleState>(initialState) {

    /*init {
        getWhistles()
    }*/

    fun getWhistles(){

    }
    companion object : MvRxViewModelFactory<WhistleViewModel, WhistleState> {
        override fun create(viewModelContext: ViewModelContext, state: WhistleState): WhistleViewModel? {
            return WhistleViewModel(state)
        }
    }
}

class WhistleFragment : BaseFragment() {
    private val viewModel: WhistleViewModel by fragmentViewModel()
    override fun epoxyController(): MvRxEpoxyController = simpleController(viewModel) {

    }

    override fun initData() {
        sharedViewModel.setVisible(false)
        viewModel.getWhistles()
    }
}