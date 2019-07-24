package com.judge.models

import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.judge.app.core.MvRxViewModel
import com.judge.data.repository.VideoRepository
import com.judge.data.state.VideoState
import com.judge.utils.LogUtils
import io.reactivex.schedulers.Schedulers

class VideoViewModel(
    initialState: VideoState,
    private val videoRepository: VideoRepository
) : MvRxViewModel<VideoState>(initialState) {

    fun fetchVideos() = withState { state ->
        if (state.isLoading) return@withState
        videoRepository.getVideos().subscribeOn(Schedulers.io())
            .doOnSubscribe {
                setState { copy(isLoading = true) }
            }
            .doOnError {
                it.message?.let { it1 -> LogUtils.e(it1) }
            }
            .doFinally { setState { copy(isLoading = false) } }
            .execute {
                copy(response = it, video = it()?.data, videos = it()?.data?.aVideo)
            }
    }

    companion object : MvRxViewModelFactory<VideoViewModel, VideoState> {
        override fun create(viewModelContext: ViewModelContext, state: VideoState): VideoViewModel? {
            val videoRepository = VideoRepository()
            return VideoViewModel(state, videoRepository)
        }
    }
}