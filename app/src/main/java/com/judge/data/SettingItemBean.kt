package com.judge.data

data class SettingItemBean @JvmOverloads constructor(
    val title: String,
    val content: String = "",
    val photoUrl: String = ""
)