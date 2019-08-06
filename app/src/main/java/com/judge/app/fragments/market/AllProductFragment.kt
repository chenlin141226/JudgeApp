package com.judge.app.fragments.market

import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.airbnb.mvrx.fragmentViewModel
import com.judge.R
import com.judge.app.core.BaseFragment
import com.judge.app.core.MvRxViewModel
import com.judge.app.core.simpleController
import com.judge.data.MarketBean
import com.judge.marketItem
import org.jetbrains.anko.collections.forEachWithIndex
import java.util.*

/**
 * @author: jaffa
 * @date: 2019/8/4
 */

data class MarketState(val marketItems: List<MarketBean> = emptyList()) : MvRxState

class MarketViewModel(initialState: MarketState) : MvRxViewModel<MarketState>(initialState) {
    private val list = LinkedList<MarketBean>()
    fun getMarket(index: Int) {
        for (i in 1 until 10) {
            val bean: MarketBean = when {
                i % 2 == 0 -> MarketBean("", "jaffa yeqi linlei$index", "库存12", "消耗了1600吊死扶")
                i % 3 == 0 -> MarketBean("", "jaffa yeqi linlei$index", "库存12", "消耗了1600吊死扶")
                else -> MarketBean("", "jaffa yeqi linlei$index", "库存12", "消耗了1600吊死扶")
            }
            list.add(bean)
        }
        setState {
            copy(marketItems = list)
        }
    }

    fun removeMarket(){
        list.clear()
        setState { copy(marketItems = list) }
    }


    companion object : MvRxViewModelFactory<MarketViewModel, MarketState> {
        override fun create(viewModelContext: ViewModelContext, state: MarketState): MarketViewModel? {
            return MarketViewModel(state)
        }
    }
}


class AllProductFragment(index: Int) : BaseFragment() {

    var index = index
    private val viewModel: MarketViewModel by fragmentViewModel()

    override fun epoxyController() = simpleController(viewModel) { state ->

            state.marketItems.forEachWithIndex { index, item ->

                marketItem {
                    id(item.marketName + index)
                    marketBean(item)
                    onClick {_ ->
                       navigateTo(R.id.action_marketFragment_to_productDetailsFragment,item)
                    }
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.removeMarket()
    }

    override fun initData() {
        viewModel.getMarket(index)

    }
}