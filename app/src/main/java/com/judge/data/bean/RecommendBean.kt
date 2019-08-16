package com.judge.data.bean

/**
 * @author: jaffa
 * @date: 2019/8/11
 */


data class RecommendBean(
    val auth: String,
    val code: String,
    val cookiepre: String,
    val `data`: List<Recommend>,
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


data class Recommend(
    val dateline: String,
    val displayorder: String,
    val pic: String,
    val tid: String,
    val title: String,
    val views: String
)