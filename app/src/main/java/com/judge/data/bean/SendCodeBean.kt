package com.judge.data.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author: jaffa
 * @date: 2019/8/14
 */
@Parcelize
data class SendCodeBean(
    val retcode: Int,
    val retmsg: String?
) : Parcelable