package com.judge.app.fragments

import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.mvrx.BaseMvRxFragment
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.judge.R
import com.judge.dogRowBinding
import com.judge.models.DogViewModel
import com.judge.views.loadingView
import kotlinx.android.synthetic.main.judge_fragment.dogsRecyclerView

class JudgeFragment : BaseMvRxFragment() {
    private val viewModel: DogViewModel by fragmentViewModel()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.judge_fragment, container, false)
    }

    override fun invalidate() = withState(viewModel) { state ->
        dogsRecyclerView.withModels {
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

}