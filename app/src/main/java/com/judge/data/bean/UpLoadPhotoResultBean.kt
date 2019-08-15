package com.judge.data.bean

data class UpLoadPhotoResultBean(
    val auth: String,
    val code: String,
    val cookiepre: String,
    val formhash: String,
    val groupid: String,
    val ismoderator: Any,
    val member_avatar: String,
    val member_uid: String,
    val member_username: String,
    val notice: Notice,
    val readaccess: String,
    val saltkey: String,
    val success: String,
    val uploadavatar: String
)
