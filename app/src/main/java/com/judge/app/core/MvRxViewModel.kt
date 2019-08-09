package com.judge.app.core

import com.airbnb.mvrx.BaseMvRxViewModel
import com.airbnb.mvrx.MvRxState
import com.judge.BuildConfig

open class MvRxViewModel<S : MvRxState>(state: S) : BaseMvRxViewModel<S>(state, debugMode = false)