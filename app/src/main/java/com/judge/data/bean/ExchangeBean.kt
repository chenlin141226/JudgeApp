package com.judge.data.bean

/**
 * @author: jaffa
 * @date: 2019/8/27
 */
data class ExchangeBean(
    val auth: String,
    val cookiepre: String,
    val `data`: ExchangeResult,
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


data class ExchangeResult(
    val code: String,
    val item: Item,
    val success: String,
    val error : String
)

data class Item(
    val cost_7ree: String,
    val detail_7ree: String,
    val displayorder_7ree: String,
    val fenlei_7ree: String,
    val id_7ree: String,
    val name_7ree: String,
    val num_7ree: String,
    val pic_7ree: String,
    val price_7ree: String
)