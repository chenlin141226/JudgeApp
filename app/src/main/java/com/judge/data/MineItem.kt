package com.judge.data

import androidx.annotation.DrawableRes

data class MineItem(
    @DrawableRes
    val leftIconIdRes: Int,
    @DrawableRes
    val rightIconIdRes: Int,
    val text: String
)