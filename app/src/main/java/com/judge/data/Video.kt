package com.judge.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class Video(
    val aVideo: List<AVideo>,
    val iTotalPageNum: Int
)

@Parcelize
data class AVideo(
    val avatar_thumb: String,
    val cate_id: String,
    val cate_name: String,
    val comments: String,
    val href: String,
    val id: String,
    val likes: String,
    val thumb_app: String,
    val thumb_h5: String,
    val title: String,
    val uid: String,
    val user_nicename: String,
    val views: Int,
    val virtual_views: String
) : Parcelable

