package com.judge.data.state

import com.airbnb.mvrx.MvRxState
import com.judge.data.AVideo

data class VideoState(
    val videos: List<AVideo>? = emptyList(),
    val isLoading: Boolean = false
) : MvRxState