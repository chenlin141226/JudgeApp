package com.judge.app.fragments

import com.airbnb.mvrx.fragmentViewModel
import com.judge.app.core.BaseFragment
import com.judge.app.core.MvRxEpoxyController
import com.judge.app.core.simpleController
import com.judge.models.VideoViewModel
import com.judge.videoRow
import com.judge.views.loadingView

class MineFragment : BaseFragment() {
    private val videoViewModel: VideoViewModel by fragmentViewModel()
    override fun epoxyController(): MvRxEpoxyController = simpleController(videoViewModel) { state ->
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