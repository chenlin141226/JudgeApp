package com.judge.app.fragments.mine

import androidx.core.view.isVisible
import com.judge.R
import com.judge.app.core.BaseFragment
import com.judge.app.core.MvRxEpoxyController
import com.judge.app.core.simpleController
import org.jetbrains.anko.sdk27.coroutines.onClick

class MedalCenterFragment : BaseFragment() {
    override fun epoxyController(): MvRxEpoxyController = simpleController {
    }

    override fun initView() {
        super.initView()
        toolbar.isVisible = true
        rightButton.apply {
            isVisible = true
            text = getString(R.string.mine)
            onClick {

            }
        }
    }

}