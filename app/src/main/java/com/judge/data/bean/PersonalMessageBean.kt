package com.judge.data.bean


data class PersonalMessageBean @JvmOverloads constructor(
    val title: String,
    val imageUrl: String = "",
    val content: String = "",
    val messageTime: String
)