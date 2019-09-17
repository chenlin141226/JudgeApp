package com.judge.app.fragments.home

import androidx.core.view.isVisible
import com.judge.R
import com.judge.app.core.BaseFragment
import com.judge.app.core.MvRxEpoxyController
import com.judge.app.core.simpleController
import org.jetbrains.anko.appcompat.v7.coroutines.onMenuItemClick
import org.jetbrains.anko.support.v4.toast

class NewsDetailFragment : BaseFragment() {
    override fun epoxyController(): MvRxEpoxyController = simpleController {
    }

    override fun initView() {
        super.initView()
        setHasOptionsMenu(true)
        toolbar.apply {
            inflateMenu(R.menu.menu_main)
            isVisible = true
            onMenuItemClick { item ->
                when (item?.itemId) {
                    R.id.action_share -> {
                        toast("share")
                    }
                    else -> {
                    }
                }
            }
        }
    }

}