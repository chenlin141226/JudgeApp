package com.judge.data

data class PublicMessageBean @JvmOverloads constructor(
    val title: String,
    val content: String = "",
    val messageTime: String
)