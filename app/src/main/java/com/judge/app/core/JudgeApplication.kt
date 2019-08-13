package com.judge.app.core

import androidx.multidex.MultiDexApplication
import com.jeremyliao.liveeventbus.LiveEventBus

import com.judge.utils.LogUtils
import com.judge.utils.NetworkUtils
import com.vondear.rxtool.RxTool

class JudgeApplication : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        LogUtils.init(this)
        RxTool.init(this)
        NetworkUtils.startNetService(this)
        LiveEventBus.get().config().lifecycleObserverAlwaysActive(false)
            .autoClear(true)
    }
}