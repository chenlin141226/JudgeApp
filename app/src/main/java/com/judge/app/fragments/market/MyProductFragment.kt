package com.judge.app.fragments.market

import android.graphics.Color
import android.view.View
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.fragmentViewModel
import com.judge.app.core.BaseFragment
import com.judge.app.core.MvRxViewModel
import com.judge.app.core.simpleController

/**
 * @author: jaffa
 * @date: 2019/8/5
 */
data class MyMarketState(
    val isLoading :Boolean = false
) : MvRxState

class MyMarketViewModel(initialState: MarketState) : MvRxViewModel<MarketState>(initialState) {

    fun getMarket() {

    }

    fun removeMarket() {

    }
}


class MyProductFragment : BaseFragment() {

    val viewModel: MyMarketViewModel by fragmentViewModel()

    override fun epoxyController() = simpleController(viewModel) { state ->

//        state.marketItems.forEachWithIndex { index, item ->
//
//            marketItem {
//                id("MyProductFragment")
//                marketBean(item)
//            }
//        }
    }

    override fun initView() {
        toolbar.visibility = View.VISIBLE
        toolbar.setBackgroundColor(Color.WHITE)
    }

    override fun initData() {
        viewModel.getMarket()
    }
}