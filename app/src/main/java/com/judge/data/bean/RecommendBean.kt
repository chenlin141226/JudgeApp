package com.judge.data.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author: jaffa
 * @date: 2019/8/11
 */

@Parcelize
data class RecommendBean(
    val info: String,
    val time: String
) : Parcelable