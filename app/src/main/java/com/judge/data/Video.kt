package com.judge.data

import com.squareup.moshi.Json

data class Video(

    @field:Json(name = "aVideo") @Json(name = "aVideo") val aVideo: List<AVideo>,
    @field:Json(name = "iTotalPageNum") @Json(name = "iTotalPageNum") val iTotalPageNum: Int
)

data class AVideo(
    @field:Json(name = "avatar_thumb") @Json(name = "avatar_thumb") val avatar_thumb: String,
    @field:Json(name = "cate_id") @Json(name = "cate_id") val cate_id: String,
    @field:Json(name = "cate_name") @Json(name = "cate_name") val cate_name: String,
    @field:Json(name = "comments") @Json(name = "comments") val comments: String,
    @field:Json(name = "href") @Json(name = "href") val href: String,
    @field:Json(name = "id") @Json(name = "id") val id: String,
    @field:Json(name = "likes") @Json(name = "likes") val likes: String,
    @field:Json(name = "thumb_app") @Json(name = "thumb_app") val thumb_app: String,
    @field:Json(name = "thumb_h5") @Json(name = "thumb_h5") val thumb_h5: String,
    @field:Json(name = "title") @Json(name = "title") val title: String,
    @field:Json(name = "uid") @Json(name = "uid") val uid: String,
    @field:Json(name = "user_nicename") @Json(name = "user_nicename") val user_nicename: String,
    @field:Json(name = "views") @Json(name = "views") val views: Int,
    @field:Json(name = "virtual_views") @Json(name = "virtual_views") val virtual_views: String
)