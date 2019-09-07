package com.judge.data.bean

/**
 * @author: jaffa
 * @date: 2019/9/7
 */
data class PushTie(
    val auth: String,
    val code: String,
    val cookiepre: String,
    val formhash: String,
    val groupid: String,
    val ismoderator: String,
    val member_avatar: String,
    val member_uid: String,
    val member_username: String,
    val notice: Notice,
    val pid: String,
    val readaccess: String,
    val saltkey: String,
    val success: String,
    val tid: String
)
