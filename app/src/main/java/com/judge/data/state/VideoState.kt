package com.judge.data.state

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Uninitialized
import com.judge.data.AVideo
import com.judge.data.Video
import com.judge.network.JsonResponse

data class VideoState(
    val videos: List<AVideo>? = emptyList(),
    val video: Video? = null,
    override val response: Async<JsonResponse<Video>> = Uninitialized,
    override val isLoading: Boolean = false
) : NetWorkState<Video>