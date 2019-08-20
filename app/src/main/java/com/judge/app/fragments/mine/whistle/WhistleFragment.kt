package com.judge.app.fragments.mine.whistle

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
import com.judge.data.bean.WhistleBean
import com.judge.data.repository.MineRepository
import com.judge.whistleItem
import org.jetbrains.anko.collections.forEachWithIndex
import org.jetbrains.anko.sdk27.coroutines.onClick


data class WhistleState(
    val whistleItems: List<WhistleBean>? = emptyList()
) : MvRxState

class WhistleViewModel(
    initialState: WhistleState
) : MvRxViewModel<WhistleState>(initialState) {

    init {
        getWhistles()
    }

    private fun getWhistles() {
        setState {
            copy(whistleItems = MineRepository.userProfile?.extcredits)
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
        state.whistleItems?.forEachWithIndex { index, item ->
            whistleItem {
                id(item.title + index)
                whistleBean(item)
            }
        }
    }

    override fun setToolBar() {
        super.setToolBar()
        toolbar.visibility = View.VISIBLE
        rightButton.apply {
            visibility = View.VISIBLE
            text = getString(R.string.whistle_rules)
            onClick {
                navigateTo(R.id.action_whistleFragment_to_whistleRulesFragment, null)
            }
        }
    }

}