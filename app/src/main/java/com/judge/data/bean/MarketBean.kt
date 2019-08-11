package com.judge.data.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MarketBean(
    val marketUrl: String,
    val marketName: String,
    val marketNumber: String,
    val marketinfo :String
) : Parcelable