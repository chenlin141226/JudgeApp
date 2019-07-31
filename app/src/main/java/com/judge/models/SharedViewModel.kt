package com.judge.models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    val visible = MutableLiveData<Boolean>()

    fun setVisible(visible: Boolean) {
        this.visible.value = visible
    }
}