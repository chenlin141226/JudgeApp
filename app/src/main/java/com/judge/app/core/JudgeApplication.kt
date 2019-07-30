package com.judge.app.core

import androidx.multidex.MultiDexApplication

import com.judge.data.repository.DogRepository
import com.judge.utils.LogUtils
import com.judge.utils.NetworkUtils
import com.vondear.rxtool.RxTool

class JudgeApplication : MultiDexApplication() {
    val dogsRepository = DogRepository()
    override fun onCreate() {
        super.onCreate()
        LogUtils.init(this)
        RxTool.init(this)
        NetworkUtils.startNetService(this)
    }
}