package com.judge.app.fragments

import com.airbnb.mvrx.fragmentViewModel
import com.judge.app.core.BaseFragment
import com.judge.app.core.MvRxEpoxyController
import com.judge.app.core.simpleController
import com.judge.dogRowBinding
import com.judge.models.DogViewModel
import com.judge.views.loadingView

class JudgeFragment : BaseFragment() {
    private val viewModel: DogViewModel by fragmentViewModel()
    override fun epoxyController(): MvRxEpoxyController = simpleController(viewModel) { state ->
        loadingView {
            id("loader")
            loading(state.isLoading)
        }
        state.dogs?.forEach {
            dogRowBinding {
                id(it.id)
                dog(it)
            }
        }
    }
}