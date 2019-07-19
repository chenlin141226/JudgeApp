package com.judge.app.fragments

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.fragmentViewModel
import com.judge.app.core.BaseFragment
import com.judge.app.core.MvRxEpoxyController
import com.judge.app.core.MvRxViewModel
import com.judge.app.core.simpleController
import com.judge.data.AVideo
import com.judge.data.repository.VideoRepository
import com.judge.videoRow
import com.judge.views.loadingView

data class VideoDetailState(val isLoading: Boolean = false, val id: String, val video: Async<AVideo> = Uninitialized) :
    MvRxState {
    /**
     * This secondary constructor will automatically called if your Fragment has
     * a parcelable in its arguments at key [com.airbnb.mvrx.MvRx.KEY_ARG].
     */
    constructor(args: AVideo) : this(id = args.id)
}

class VideoDetailViewModel(
    initialState: VideoDetailState,
    private val videoRepository: VideoRepository
) : MvRxViewModel<VideoDetailState>(initialState) {
    init {

    }
}

class DetailFragment : BaseFragment() {
    private val videoDetailViewModel: VideoDetailViewModel by fragmentViewModel()
    override fun epoxyController(): MvRxEpoxyController = simpleController(videoDetailViewModel) { state ->
        loadingView {
            id("loader")
            loading(state.isLoading)
        }
        videoRow {

        }
    }

}