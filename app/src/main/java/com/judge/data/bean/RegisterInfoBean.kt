package com.judge.data.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author: jaffa
 * @date: 2019/8/14
 */
@Parcelize
data class RegisterInfoBean @JvmOverloads constructor(
    val username: String? = null,
    val password: String? = null,
    val confirmPwd: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val phoneCode: String? = null,
    val code: String? = null
) : Parcelable