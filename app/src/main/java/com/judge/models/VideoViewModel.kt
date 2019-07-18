package com.judge.models

import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.judge.app.core.MvRxViewModel
import com.judge.data.repository.VideoRepository
import com.judge.data.state.VideoState
import io.reactivex.schedulers.Schedulers

class VideoViewModel(
    initialState: VideoState,
    private val videoRepository: VideoRepository
) : MvRxViewModel<VideoState>(initialState) {
    init {
        fetchVideos()
    }

    private fun fetchVideos() = withState {
        if (it.isLoading) return@withState
        videoRepository.getVideos().subscribeOn(Schedulers.io())
            .doOnSubscribe { setState { copy(isLoading = true) } }
            .doOnComplete { setState { copy(isLoading = false) } }
            .execute {
                copy(videos = it()?.aVideo)
            }
    }

    companion object : MvRxViewModelFactory<VideoViewModel, VideoState> {
        override fun create(viewModelContext: ViewModelContext, state: VideoState): VideoViewModel? {
            val videoRepository = VideoRepository()
            return VideoViewModel(state, videoRepository)
        }
    }
}