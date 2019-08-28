package com.judge.data.bean

/**
 * @author: jaffa
 * @date: 2019/8/27
 */
data class MyProductBean(
    val auth: String,
    val code: String,
    val cookiepre: String,
    val `data`: List<MyProduct>,
    val formhash: String,
    val groupid: String,
    val ismoderator: Any,
    val member_avatar: String,
    val member_uid: String,
    val member_username: String,
    val notice: Notice,
    val page: String,
    val readaccess: String,
    val saltkey: String,
    val success: String,
    val total_page: String,
    val total_per_page: String
)

data class MyProduct(
    val cost_7ree: String,
    val gid_7ree: String,
    val goods_7ree: Goods7ree,
    val id_7ree: String,
    val ip_7ree: String,
    val status_7ree: String,
    val time_7ree: String,
    val uid_7ree: String,
    val user_7ree: String
)

data class Goods7ree(
    val id_7ree: String,
    val name_7ree: String,
    val pic_7ree: String
)