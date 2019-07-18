package com.judge.app.fragments

import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.mvrx.*
import com.judge.R
import com.judge.models.DogViewModel
import com.judge.views.dogRow
import com.judge.views.loadingView
import kotlinx.android.synthetic.main.home_fragment.*

class HomeFragment : BaseMvRxFragment() {
    private val viewModel: DogViewModel by fragmentViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun invalidate() = withState(viewModel) { state ->

        dogsRecyclerView.withModels {
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

}