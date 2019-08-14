package com.judge.data.bean

data class NewsBean(
    val `data`: List<News>,
    val auth: Any,
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
    val success: String
)


data class News(
    val attachment: String,
    val author: String,
    val authorid: String,
    val dateline: String,
    val dbdateline: String,
    val dblastpost: String,
    val digest: String,
    val lastpost: String,
    val lastposter: String,
    val readperm: String,
    val replies: String,
    val subject: String,
    val tid: String,
    val views: String
)