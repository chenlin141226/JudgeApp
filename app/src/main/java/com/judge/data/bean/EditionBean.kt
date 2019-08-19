package com.judge.data.bean

data class EditionBean(
    val auth: String,
    val catlist: List<Catlist>,
    val code: String,
    val cookiepre: String,
    val formhash: String,
    val forumlist: List<Forumlist>,
    val group: Group,
    val groupid: String,
    val ismoderator: Any,
    val member_avatar: String,
    val member_credits: String,
    val member_email: String,
    val member_uid: String,
    val member_username: String,
    val notice: Notice,
    val readaccess: String,
    val saltkey: String,
    val setting_bbclosed: String,
    val success: String
)

data class Catlist(
    val fid: String,
    val forums: List<String>,
    val name: String
)


data class Forumlist(
    val description: String,
    val fid: String,
    val icon: String,
    val name: String,
    val posts: String,
    val threads: String,
    val todayposts: String,
    val favorite: String
)