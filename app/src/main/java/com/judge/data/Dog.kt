package com.judge.data

import android.graphics.Color


data class Dog(
    val id: Long,
    val name: String?,
    val breeds: String?,
    val imageUrl: String?,
    val description: String?,
    val color: Int = Color.GREEN
)