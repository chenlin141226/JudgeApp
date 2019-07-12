package com.judge.data.state

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import com.judge.data.AVideo
import com.judge.data.Video

data class VideoState(
    val video: Async<Video> = Uninitialized,
    val videos: List<AVideo>? = emptyList()
) : MvRxState