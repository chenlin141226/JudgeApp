package com.judge.data.bean

data class InformationBean(
    val auth: Any,
    val cookiepre: String,
    val `data`: List<Data>,
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


data class Data(
    val dateline: String,
    val displayorder: String,
    val pic: String,
    val tid: String,
    val title: String,
    val views: String
)