package com.judge.data

data class MineDataBean(
    val auth: Any,
    val cookiepre: String,
    val extcredits: Extcredits,
    val formhash: String,
    val groupid: String,
    val ismoderator: Any,
    val member_avatar: String,
    val member_uid: String,
    val member_username: String,
    val notice: Notice,
    val qiandaodb: List<Any>,
    val readaccess: String,
    val saltkey: String,
    val space: Space,
    val wsq: Wsq
)

data class Notice(
    val newmypost: String,
    val newpm: String,
    val newprompt: String,
    val newpush: String
)

data class Extcredits(
    val `1`: X1,
    val `2`: X2,
    val `3`: X3,
    val `4`: X4
)

data class X4(
    val allowexchangein: Any,
    val allowexchangeout: Any,
    val img: String,
    val ratio: String,
    val showinthread: Any,
    val title: String,
    val unit: String
)

data class X3(
    val allowexchangein: Any,
    val allowexchangeout: Any,
    val img: String,
    val ratio: String,
    val showinthread: Any,
    val title: String,
    val unit: String
)

data class X2(
    val allowexchangein: Any,
    val allowexchangeout: Any,
    val img: String,
    val ratio: String,
    val showinthread: Any,
    val title: String,
    val unit: String
)

data class X1(
    val allowexchangein: Any,
    val allowexchangeout: Any,
    val img: String,
    val ratio: String,
    val showinthread: Any,
    val title: String,
    val unit: String
)

data class Wsq(
    val wsq_apicredit: Any
)

data class Space(
    val groupiconid: Any
)