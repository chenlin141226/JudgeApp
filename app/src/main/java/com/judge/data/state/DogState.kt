package com.judge.data.state

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import com.judge.data.Dog

data class DogState(
    val dogs: Async<List<Dog>> = Uninitialized
) : MvRxState