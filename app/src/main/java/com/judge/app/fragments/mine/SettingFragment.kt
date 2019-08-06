package com.judge.app.fragments.mine

import androidx.core.view.isVisible
import com.judge.app.core.BaseFragment
import com.judge.app.core.MvRxEpoxyController
import com.judge.app.core.simpleController

class SettingFragment : BaseFragment() {
    override fun epoxyController(): MvRxEpoxyController = simpleController {
    }

    override fun initView() {
        super.initView()
        toolbar.isVisible = true
    }
}