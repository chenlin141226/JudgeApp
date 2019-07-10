package com.judge.app.fragments

import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.airbnb.mvrx.BaseMvRxFragment
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.judge.R
import com.judge.dogRowBinding
import com.judge.models.DogViewModel
import kotlinx.android.synthetic.main.judge_fragment.dogsRecyclerView
import kotlinx.android.synthetic.main.judge_fragment.loadingAnimation

class JudgeFragment : BaseMvRxFragment() {
    private val viewModel: DogViewModel by fragmentViewModel()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.judge_fragment, container, false)
    }

    override fun invalidate() = withState(viewModel) { state ->
        loadingAnimation.isVisible = state.dogs is Loading
        dogsRecyclerView.withModels {
            state.dogs()?.forEach {
                dogRowBinding {
                    id(it.id)
                    dog(it)
                }
            }
        }
    }

}