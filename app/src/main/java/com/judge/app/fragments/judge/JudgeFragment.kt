package com.judge.app.fragments.judge

import android.view.View
import com.judge.app.core.BaseFragment
import com.judge.app.core.MvRxEpoxyController
import com.judge.app.core.simpleController

class JudgeFragment : BaseFragment() {


    override fun epoxyController(): MvRxEpoxyController = simpleController {
    }

    override fun setToolBar() {
        super.setToolBar()
        toolbar.visibility = View.VISIBLE
    }

    override fun initData() {

    }
}