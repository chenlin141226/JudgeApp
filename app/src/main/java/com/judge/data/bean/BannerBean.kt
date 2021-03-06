package com.judge.data.bean

data class BannerBean(
    val `data`: List<Banner>,
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


data class Banner(
    val displayorder: String,
    val pic: String,
    val title: String
)
