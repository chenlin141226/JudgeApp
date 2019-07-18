package com.judge.app.fragments

import com.airbnb.mvrx.*
import com.judge.app.core.BaseFragment
import com.judge.app.core.MvRxEpoxyController
import com.judge.app.core.simpleController
import com.judge.models.DogViewModel
import com.judge.views.dogRow
import com.judge.views.loadingView

class HomeFragment : BaseFragment() {
    private val viewModel: DogViewModel by fragmentViewModel()
    override fun epoxyController(): MvRxEpoxyController = simpleController(viewModel) { state ->
        loadingView {
            id("loader")
            loading(state.isLoading)
        }
        state.dogs?.forEachIndexed { index, dog ->
            dogRow {
                id(dog.id)
                dog(dog)
                textColor(dog.color)
                clickListener { _ ->
                    viewModel.setItemState(index, dog)
                }
            }
        }
    }

}