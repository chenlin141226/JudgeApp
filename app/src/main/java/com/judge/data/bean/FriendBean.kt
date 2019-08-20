package com.judge.data.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class FriendBean(
    val auth: String,
    val code: String,
    val cookiepre: String,
    val count: String,
    val formhash: String,
    val groupid: String,
    val ismoderator: Any,
    val list: List<Friend>,
    val member_avatar: String,
    val member_uid: String,
    val member_username: String,
    val notice: Notice,
    val readaccess: String,
    val saltkey: String,
    val success: String
)

@Parcelize
data class Friend(
    val uid: String,
    val username: String,
    val avatar: String
):Parcelable