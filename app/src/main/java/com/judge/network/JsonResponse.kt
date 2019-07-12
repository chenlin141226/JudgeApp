package com.judge.network

import com.squareup.moshi.Json

data class JsonResponse<T>(
    @field:Json(name = "code") @Json(name = "code") val code: Int,
    @field:Json(name = "msg") @Json(name = "msg") val msg: String,
    @field:Json(name = "data") @Json(name = "data") val data: T
)