package com.judge.data.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class MarketInfoBean<T>(
    val auth: String,
    val code: String,
    val cookiepre: String,
    val `data`: DataItems,
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

data class DataItems(
    val fenlei: List<Fenlei>,
    val item: List<CategoryItem>
)

data class Fenlei(
    val fenlei_title: String,
    val id: Int
)

@Parcelize
data class CategoryItem @JvmOverloads constructor(
    val cost_7ree: String,
    val detail_7ree: String,
    val displayorder_7ree: String,
    val fenlei_7ree: String,
    val id_7ree: String,
    val name_7ree: String,
    val num_7ree: String,
    val pic_7ree: String,
    val price_7ree: String
):Parcelable
