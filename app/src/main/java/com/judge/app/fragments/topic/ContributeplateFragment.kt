package com.judge.app.fragments.topic

import androidx.core.view.isVisible
import com.airbnb.mvrx.MvRxState
import com.judge.app.core.BaseFragment
import com.judge.app.core.simpleController

/**
 * @author: jaffa
 * @date: 2019/9/6
 */
data class PlateState(
    val isLoading : Boolean = false
) : MvRxState
class ContributeplateFragment : BaseFragment() {

    override fun epoxyController() = simpleController {

    }

    override fun initView() {
        super.initView()
        toolbar.isVisible = true
        toolbar.title = "投稿模板"
    }
}