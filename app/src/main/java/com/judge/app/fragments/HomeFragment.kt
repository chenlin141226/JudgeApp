package com.judge.app.fragments

import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.airbnb.mvrx.*
import com.judge.R
import com.judge.models.DogViewModel
import com.judge.views.dogRow
import kotlinx.android.synthetic.main.home_fragment.*

class HomeFragment : BaseMvRxFragment() {
    private val viewModel: DogViewModel by fragmentViewModel ()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun invalidate() = withState(viewModel) { state ->
        loadingAnimation.isVisible = state.dogs is Loading
        dogsRecyclerView.withModels {
            state.dogs()?.forEach {
                dogRow {
                    id(it.id)
                    dog(it)
                }
            }
        }
    }

}