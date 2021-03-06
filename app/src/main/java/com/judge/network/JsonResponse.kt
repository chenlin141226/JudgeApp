package com.judge.network

data class JsonResponse<T>(
    val Version: String,
    val Charset: String,
    val Variables: T,
    val Message: Message
)

data class Message(
    val messagestr: String,
    val messageval: String
)