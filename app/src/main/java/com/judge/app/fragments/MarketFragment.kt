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
import com.judge.models.VideoViewModel
import com.judge.videoRow
import com.judge.views.loadingView
import kotlinx.android.synthetic.main.market_fagment.*

class MarketFragment : BaseMvRxFragment() {
    private val videoViewModel: VideoViewModel by fragmentViewModel()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.market_fagment, container, false)
    }

    override fun invalidate() = withState(videoViewModel) { state ->
        videosRecyclerView.withModels {
            loadingView {
                id("loader")
                loading(state.isLoading)
            }
            state.videos?.forEach {
                videoRow {
                    id(it.id)
                    video(it)
                }
            }

        }
    }
}