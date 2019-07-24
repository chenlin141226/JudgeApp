package com.judge.network

data class JsonResponse<T>(
    val code: Int,
    val msg: String,
    val data: T
)