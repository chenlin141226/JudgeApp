package com.judge.app.core

import android.app.Application

import com.judge.data.repository.DogRepository
import com.judge.utils.AppUtils
import com.judge.utils.LogUtils
import com.judge.utils.NetworkUtils

class JudgeApplication : Application() {
    val dogsRepository = DogRepository()
    override fun onCreate() {
        super.onCreate()
        AppUtils.init(this)
        LogUtils.init(this)
        NetworkUtils.startNetService(this)
    }
}