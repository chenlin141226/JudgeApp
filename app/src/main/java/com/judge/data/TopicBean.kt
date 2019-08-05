package com.judge.data

data class TopicBean @JvmOverloads constructor(
    val title: String,
    val content: String = "",
    val topicUserName: String,
    val time: String,
    val viewedCount: String,
    val commentCount: String
)