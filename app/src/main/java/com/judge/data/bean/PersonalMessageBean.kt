package com.judge.data.bean


data class PersonalMessageBean(
    val auth: String,
    val code: String,
    val cookiepre: String,
    val count: String,
    val formhash: String,
    val groupid: String,
    val ismoderator: Any,
    val list: List<PersonalMessage>,
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

data class PersonalMessage(
    val isnew: String,
    val message: String,
    val msgfrom: String,
    val msgfromid: String,
    val plid: String,
    val pmid: String,
    val subject: String,
    val touid: String,
    val tousername: String,
    val vdateline: String,
    val avatar: String
)
