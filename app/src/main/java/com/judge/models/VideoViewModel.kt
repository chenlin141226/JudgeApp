package com.judge.models

import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.judge.app.core.MvRxViewModel
import com.judge.data.state.VideoState
import com.judge.network.ServiceCreator
import com.judge.network.services.VideoService
import io.reactivex.schedulers.Schedulers

class VideoViewModel(
    initialState: VideoState,
    private val videoService: VideoService
) : MvRxViewModel<VideoState>(initialState) {
    init {
        fetchVideos()
    }

    private fun fetchVideos() = withState { state ->
        if (state.response is Loading) return@withState
        val map = HashMap<String, String>()
        map["timeStamp"] = "1559550233582"
        map["secretKey"] = "c9bf9964dd953dee4219801157dec08f"
        map["pageNum"] = "1"
        map["cate_id"] = "0"
        map["iUid"] = "0"
        videoService.getVideos(map).subscribeOn(Schedulers.io()).execute {
            copy(response = it, video = it()?.data, videos = it()?.data?.aVideo)
        }

    }

    companion object : MvRxViewModelFactory<VideoViewModel, VideoState> {
        override fun create(viewModelContext: ViewModelContext, state: VideoState): VideoViewModel? {
            val videoService = ServiceCreator.create(VideoService::class.java)
            return VideoViewModel(state, videoService)
        }
    }
}