package com.judge.data.bean

data class MedalBean(
    val `data`: List<Medal>,
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
    val saltkey: String
)

data class Medal(
    val available: String,
    val credit: String,
    val description: String,
    val expiration: String,
    val image: String,
    val medalid: String,
    val name: String,
    val price: String,
    val type: String
)