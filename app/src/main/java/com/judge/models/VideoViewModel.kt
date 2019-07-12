package com.judge.models

import com.airbnb.mvrx.Loading
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

    private fun fetchVideos() = withState { state ->
        if (state.video is Loading) return@withState

        /*videoService.getVideos(map).subscribeOn(Schedulers.io()).execute {
            copy(response = it, video = it()?.data, videos = it()?.data?.aVideo)
        }*/
        videoRepository.getVideos().subscribeOn(Schedulers.io()).execute {
            copy(video = it, videos = it()?.aVideo)
        }
    }

    companion object : MvRxViewModelFactory<VideoViewModel, VideoState> {
        override fun create(viewModelContext: ViewModelContext, state: VideoState): VideoViewModel? {
            val videoRepository = VideoRepository()
            return VideoViewModel(state, videoRepository)
        }
    }
}