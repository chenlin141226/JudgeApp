package com.judge.data.bean

data class PublicMessageBean(
    val auth: String,
    val code: String,
    val cookiepre: String,
    val count: String,
    val formhash: String,
    val groupid: String,
    val ismoderator: Any,
    val list: List<PublicMessage>,
    val member_avatar: String,
    val member_uid: String,
    val member_username: String,
    val notice: Notice,
    val page: String,
    val perpage: String,
    val readaccess: String,
    val saltkey: String,
    val success: String
)

data class PublicMessage(
    val id: String,
    val authorid: String,
    val author: String,
    val dateline: String,
    val message: String
)