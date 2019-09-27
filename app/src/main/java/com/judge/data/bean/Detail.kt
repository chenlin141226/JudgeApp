package com.judge.data.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author: jaffa
 * @date: 2019/9/27
 */
@Parcelize
data class Detail(
    var tid : String = ""
): Parcelable
