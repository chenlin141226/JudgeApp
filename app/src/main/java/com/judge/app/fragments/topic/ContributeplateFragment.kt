package com.judge.app.fragments.topic

import androidx.core.view.isVisible
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.judge.R
import com.judge.app.core.BaseFragment
import com.judge.app.core.MvRxViewModel
import com.judge.app.core.simpleController
import com.judge.plateItem

/**
 * @author: jaffa
 * @date: 2019/9/6
 */
data class PlateState(
    val isLoading: Boolean = false
) : MvRxState

class PlateViewModel(initialiState: PlateState) : MvRxViewModel<PlateState>(initialiState) {
    companion object : MvRxViewModelFactory<PlateViewModel, PlateState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: PlateState
        ): PlateViewModel? {
            return PlateViewModel(state)
        }
    }
}

class ContributeplateFragment : BaseFragment() {

    override fun epoxyController() = simpleController {
        plateItem {
            id("attentions")
            title(resources.getString(R.string.attention_plate))
        }

        plateItem {
            id("recommend")
            title(resources.getString(R.string.recommend_plate))
        }
    }

    override fun initView() {
        super.initView()
        toolbar.isVisible = true
        toolbar.title = "投稿模板"
    }
}