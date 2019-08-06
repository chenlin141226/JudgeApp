package com.judge.app.fragments.market

import android.graphics.Color
import android.view.View
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.fragmentViewModel
import com.judge.app.core.BaseFragment
import com.judge.app.core.MvRxViewModel
import com.judge.app.core.simpleController
import com.judge.data.MarketBean
import com.judge.marketItem
import org.jetbrains.anko.collections.forEachWithIndex
import java.util.*

/**
 * @author: jaffa
 * @date: 2019/8/5
 */
data class MyMarketState(val marketItems: List<MarketBean> = emptyList()) : MvRxState

class MyMarketViewModel(initialState: MarketState) : MvRxViewModel<MarketState>(initialState) {
    private val list = LinkedList<MarketBean>()
    fun getMarket() {
        for (i in 1 until 9) {
            val bean: MarketBean = when {
                i % 2 == 0 -> MarketBean("", "jaffa yeqi linlei", "库存12", "消耗了1600吊死扶")
                i % 3 == 0 -> MarketBean("", "jaffa yeqi linlei", "库存12", "消耗了1600吊死扶")
                else -> MarketBean("", "jaffa yeqi linlei", "库存12", "消耗了1600吊死扶")
            }
            list.add(bean)
        }
        setState {
            copy(marketItems = list)
        }
    }

    fun removeMarket() {
        list.clear()
        setState { copy(marketItems = list) }
    }
}


class MyProductFragment : BaseFragment() {

    val viewModel: MyMarketViewModel by fragmentViewModel()

    override fun epoxyController() = simpleController(viewModel) { state ->

        state.marketItems.forEachWithIndex { index, item ->

            marketItem {
                id("MyProductFragment")
                marketBean(item)
            }
        }
    }

    override fun initView() {
        toolbar.visibility = View.VISIBLE
        toolbar.setBackgroundColor(Color.WHITE)
    }

    override fun initData() {
        viewModel.getMarket()
    }
}