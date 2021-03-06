package com.judge.data.bean

data class ProfileBean(
    val auth: String,
    val code: String,
    val cookiepre: String,
    val extcredits: List<WhistleBean>,
    val formhash: String,
    val groupid: String,
    val ismoderator: Any,
    val member_avatar: String,
    val member_uid: String,
    val member_username: String,
    val notice: Notice,
    val qiandaodb: Qiandaodb,
    val readaccess: String,
    val saltkey: String,
    val space: ProfileDetail,
    val success: String,
    val wsq: Wsq
)

data class Qiandaodb(
    val days: String,
    val lasted: String,
    val lastreward: String,
    val mdays: String,
    val qdxq: String,
    val reward: String,
    val time: String,
    val todaysay: String,
    val uid: String,
    val level: String
)

data class Wsq(
    val wsq_apicredit: Any
)

data class ProfileDetail(
    val acceptemail: List<Any>,
    val accessmasks: String,
    val addfriend: String,
    val address: String,
    val addsize: String,
    val admingroup: Admingroup,
    val adminid: String,
    val affectivestatus: String,
    val albums: String,
    val alipay: String,
    val allowadmincp: String,
    val attachsize: String,
    val attentiongroup: String,
    val authstr: String,
    val avatarstatus: String,
    val bio: String,
    val birthcity: String,
    val birthcommunity: String,
    val birthday: String,
    val birthdist: String,
    val birthmonth: String,
    val birthprovince: String,
    val birthyear: String,
    val blacklist: String,
    val blockposition: String,
    val blogs: String,
    val bloodtype: String,
    val buyercredit: String,
    val buyerrank: String,
    val company: String,
    val conisbind: String,
    val constellation: String,
    val credits: String,
    val customshow: String,
    val customstatus: String,
    val digestposts: String,
    val doings: String,
    val domain: String,
    val education: String,
    val emailstatus: String,
    val extcredits1: String,
    val extcredits2: String,
    val extcredits3: String,
    val extcredits4: String,
    val extcredits5: String,
    val extcredits6: String,
    val extcredits7: String,
    val extcredits8: String,
    val extgroupids: String,
    val favtimes: String,
    val feedfriend: String,
    val feeds: String,
    val field1: String,
    val field2: String,
    val field3: String,
    val field4: String,
    val field5: String,
    val field6: String,
    val field7: String,
    val field8: String,
    val follower: String,
    val following: String,
    val freeze: String,
    val friends: String,
    val gender: String,
    val graduateschool: String,
    val group: Group,
    val groupexpiry: String,
    val groupiconid: String,
    val groupid: String,
    val groups: String,
    val groupterms: String,
    val height: String,
    val icq: String,
    val idcard: String,
    val idcardtype: String,
    val interest: String,
    val invisible: String,
    val lastactivity: String,
    val lastactivitydb: String,
    val lastpost: String,
    val lastsendmail: String,
    val lastvisit: String,
    val lookingfor: String,
    val magicgift: String,
    val medals: List<Medals>,
    val menunum: String,
    val mobile: String,
    val msn: String,
    val nationality: String,
    val newfollower: String,
    val newpm: String,
    val newprompt: String,
    val notifysound: String,
    val occupation: String,
    val oltime: String,
    val onlyacceptfriendpm: String,
    val port: String,
    val position: String,
    val posts: String,
    val privacy: Privacy,
    val profileprogress: String,
    val publishfeed: String,
    val qq: String,
    val realname: String,
    val recentnote: String,
    val regdate: String,
    val residecity: String,
    val residecommunity: String,
    val residedist: String,
    val resideprovince: String,
    val residesuite: String,
    val revenue: String,
    val self: String,
    val sellercredit: String,
    val sellerrank: String,
    val sharetimes: String,
    val sharings: String,
    val sightml: String,
    val site: String,
    val spacecss: String,
    val spacedescription: String,
    val spacename: String,
    val spacenote: String,
    val status: String,
    val stickblogs: String,
    val taobao: String,
    val telephone: String,
    val theme: String,
    val threads: String,
    val timeoffset: String,
    val todayattachs: String,
    val todayattachsize: String,
    val uid: String,
    val username: String,
    val videophoto: String,
    val videophotostatus: String,
    val views: String,
    val weight: String,
    val yahoo: String,
    val zipcode: String,
    val zodiac: String,
    val birthdate: String,
    val extcredits : List<Extcredits>
)

data class Extcredits(
    val img: String,
    val title: String,
    val ratio: String
)

data class Medals(
    val description: String,
    val image: String,
    val medalid: String,
    val name: String
)


data class Admingroup(
    val allowbegincode: String,
    val allowgetattach: String,
    val allowgetimage: String,
    val allowmediacode: String,
    val color: String,
    val grouptitle: String,
    val icon: String,
    val maxsigsize: String,
    val readaccess: String,
    val stars: String,
    val type: String,
    val userstatusby: String
)

data class Privacy(
    val feed: Feed,
    val profile: Profile,
    val view: View
)

data class Feed(
    val doing: String,
    val newreply: String,
    val newthread: String,
    val upload: String
)

data class Profile(
    val birthday: String,
    val gender: String,
    val realname: String,
    val residecity: String
)

data class View(
    val album: String,
    val blog: String,
    val doing: String,
    val friend: String,
    val home: String,
    val index: String,
    val share: String,
    val wall: String
)

data class Group(
    val allowbegincode: String,
    val allowgetattach: String,
    val allowgetimage: String,
    val allowmediacode: String,
    val color: String,
    val grouptitle: String,
    val icon: String,
    val maxsigsize: String,
    val readaccess: String,
    val stars: String,
    val type: String,
    val userstatusby: String
)

data class Notice(
    val newmypost: String,
    val newpm: String,
    val newprompt: String,
    val newpush: String
)

/*
data class Extcredit(
    val allowexchangein: Any,
    val allowexchangeout: Any,
    val img: String,
    val ratio: String,
    val showinthread: Any,
    val title: String,
    val unit: String
)*/
