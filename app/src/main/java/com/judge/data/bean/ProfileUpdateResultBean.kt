package com.judge.data.bean

data class ProfileUpdateResultBean(
    val `data`: ProfileUpdateResult,
    val auth: String,
    val code: String,
    val cookiepre: String,
    val formhash: String,
    val groupid: String,
    val ismoderator: Any,
    val member_avatar: String,
    val member_uid: String,
    val member_username: String,
    val notice: Notice,
    val readaccess: String,
    val saltkey: String,
    val success: String
)

data class ProfileUpdateResult(
    val address: Address,
    val birthdate: Birthdate,
    val gender: Gender,
    val mobile: Mobile,
    val qq: Qq,
    val realname: Realname
)

data class Mobile(
    val privacy: Any,
    val value: String
)

data class Address(
    val privacy: Any,
    val value: String
)

data class Qq(
    val privacy: Any,
    val value: String
)

data class Birthdate(
    val birthday: String,
    val birthmonth: String,
    val birthyear: String,
    val privacy: Any
)

data class Gender(
    val privacy: Any,
    val value: String
)

data class Realname(
    val privacy: Any,
    val value: String
)
