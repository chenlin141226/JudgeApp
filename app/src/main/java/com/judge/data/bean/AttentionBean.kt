package com.judge.data.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class AttentionBean(
    val auth: String,
    val code: String,
    val cookiepre: String,
    val count: String,
    val formhash: String,
    val groupid: String,
    val ismoderator: Any,
    val list: List<Attention>,
    val member_avatar: String,
    val member_uid: String,
    val member_username: String,
    val notice: Notice,
    val perpage: String,
    val readaccess: String,
    val saltkey: String,
    val success: String
)

@Parcelize
data class Attention(
    val dateline: String,
    val description: String,
    val favid: String,
    val icon: String,
    val id: String,
    val idtype: String,
    val posts: String,
    val spaceuid: String,
    val threads: String,
    val title: String,
    val todayposts: String,
    val uid: String,
    val url: String,
    val yesterdayposts: String
):Parcelable