package com.judge.app.fragments.mine.medal

import androidx.core.view.isVisible
import com.airbnb.mvrx.fragmentViewModel
import com.judge.app.core.BaseFragment
import com.judge.app.core.MvRxEpoxyController
import com.judge.app.core.simpleController
import com.judge.medalItemView
import com.judge.noMedalView
import org.jetbrains.anko.collections.forEachWithIndex

class MineMedalFragment : BaseFragment() {
    private val viewModel: MedalViewModel by fragmentViewModel()
    override fun epoxyController(): MvRxEpoxyController = simpleController(viewModel) { state ->
        if (state.isLoading) {
            loadingDialog.show()
        } else {
            loadingDialog.dismiss()
        }
        if (state.medals.isNullOrEmpty()) {
            noMedalView {
                id("no medal")
            }
        }
        state.medals?.forEachWithIndex { index, medal ->
            medalItemView {
                id(medal.name + index)
                medal(medal)
            }
        }
    }

    override fun initView() {
        super.initView()
        toolbar.isVisible = true
    }
}