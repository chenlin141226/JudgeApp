package com.judge.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WhistleBean(
    val whistleName: String,
    val whistleType: String,
    val whistleNumber: Int
) : Parcelable