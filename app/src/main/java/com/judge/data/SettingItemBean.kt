package com.judge.data

import android.net.Uri

data class SettingItemBean @JvmOverloads constructor(
    val title: String,
    val content: String = "",
    val photoUrl: String = "",
    val photoUri: Uri? = null
)