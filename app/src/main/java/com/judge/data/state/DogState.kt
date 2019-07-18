package com.judge.data.state

import com.airbnb.mvrx.MvRxState
import com.judge.data.Dog

data class DogState(
    val dogs: List<Dog>? = emptyList(),
    val isLoading: Boolean = false
) : MvRxState