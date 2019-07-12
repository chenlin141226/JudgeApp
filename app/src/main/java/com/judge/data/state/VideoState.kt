package com.judge.data.state

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import com.judge.data.AVideo
import com.judge.data.Video
import com.judge.network.JsonResponse

data class VideoState(
    val response: Async<JsonResponse<Video>> = Uninitialized,
    val video: Video? = null,
    val videos: List<AVideo>? = emptyList()
) : MvRxState