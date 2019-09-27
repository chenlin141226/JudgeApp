package com.judge.data.bean

/**
 * @author: jaffa
 * @date: 2019/9/6
 */
data class PlateBean(
    val auth: String,
    val catlist: List<Catlist>,
    val code: String,
    val cookiepre: String,
    val formhash: String,
    val forumlist: List<PlateCategory>,
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


data class PlateCategory(
    val description: String,
    val favorite: String,
    val fid: String,
    val icon: String,
    val name: String,
    val posts: String,
    val threads: String,
    val todayposts: String,
    val typeid : String
)