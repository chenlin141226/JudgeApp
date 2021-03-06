package com.judge.data.bean

data class FriendsMessageBean(
    val auth: String,
    val code: String,
    val cookiepre: String,
    val count: String,
    val formhash: String,
    val groupid: String,
    val ismoderator: Any,
    val list: List<FriendMessage>,
    val member_avatar: String,
    val member_uid: String,
    val member_username: String,
    val notice: Notice,
    val page: String,
    val perpage: String,
    val pmid: String,
    val readaccess: String,
    val saltkey: String,
    val success: String
)


data class FriendMessage(
    val avatar: String,
    val message: String,
    val msgfrom: String,
    val msgfromid: String,
    val plid: String,
    val pmid: String,
    val subject: String,
    val touid: String,
    val vdateline: String
)