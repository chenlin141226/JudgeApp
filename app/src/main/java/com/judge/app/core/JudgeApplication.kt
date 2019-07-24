package com.judge.app.core

import androidx.multidex.MultiDexApplication

import com.judge.data.repository.DogRepository
import com.judge.utils.AppUtils
import com.judge.utils.LogUtils
import com.judge.utils.NetworkUtils

class JudgeApplication : MultiDexApplication() {
    val dogsRepository = DogRepository()
    override fun onCreate() {
        super.onCreate()
        AppUtils.init(this)
        LogUtils.init(this)
        NetworkUtils.startNetService(this)
    }
}