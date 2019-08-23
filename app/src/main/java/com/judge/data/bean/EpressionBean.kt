package com.judge.data.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author: jaffa
 * @date: 2019/8/22
 */
data class EpressionBean(
    val auth: String,
    val code: String,
    val cookiepre: String,
    val `data`: DataItem,
    val formhash: String,
    val groupid: String,
    val ismoderator: Any,
    val member_avatar: String,
    val member_uid: String,
    val member_username: String,
    val notice: Notice,
    val readaccess: String,
    val saltkey: String,
    val success: String
)

data class DataItem(
    val level: String,
    val `open`: String,
    val smiley: List<Smiley>
)
@Parcelize
data class Smiley(
    val count: String,
    val displayorder: String,
    val emoticon: String,
    val id: String,
    val name: String,
    val qdxq: String
):Parcelable

