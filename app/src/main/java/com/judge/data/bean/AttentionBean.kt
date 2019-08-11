package com.judge.data.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AttentionBean(
    val attentionUrl: String,
    val attentionName: String,
    val attentionNumber: String,
    val attentionInfo :String
) : Parcelable