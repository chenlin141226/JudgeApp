package com.judge.data.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author: jaffa
 * @date: 2019/9/5
 */
@Parcelize
data class LoginStatus @JvmOverloads constructor(
    val code: String,
    val cookiepre: String,
    val formhash: String,
    val groupid: String,
    val loginUrl: String,
    val member_avatar: String,
    val member_uid: String,
    val member_username: String,
    val readaccess: String,
    val saltkey: String,
    val success: String
):Parcelable

