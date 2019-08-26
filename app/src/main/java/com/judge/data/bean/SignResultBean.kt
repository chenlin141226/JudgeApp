package com.judge.data.bean

/**
 * @author: jaffa
 * @date: 2019/8/23
 */
data class SignResultBean(
    val auth: String,
    val code: String,
    val cookiepre: String,
    val `data`: SignResult,
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


data class SignResult(
    val level: String,
    val msg: String,
    val `open`: String,
    val submit: String
)