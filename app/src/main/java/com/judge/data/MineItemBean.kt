package com.judge.data

import androidx.annotation.DrawableRes

data class MineItemBean(
    @DrawableRes
    val leftIconIdRes: Int,
    @DrawableRes
    val rightIconIdRes: Int,
    val text: String
)