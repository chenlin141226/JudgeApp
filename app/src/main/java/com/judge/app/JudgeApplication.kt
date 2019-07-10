package com.judge.app

import androidx.multidex.MultiDexApplication
import com.judge.data.DogRepository

class JudgeApplication : MultiDexApplication() {
    val dogsRepository = DogRepository()
}