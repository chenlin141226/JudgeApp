package com.judge.data.state

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.judge.network.JsonResponse

interface NetWorkState<T> : MvRxState{
    val response: Async<JsonResponse<T>>
    val isLoading: Boolean
}