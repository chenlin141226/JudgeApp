package com.judge.data.bean

data class TopicBean constructor(
    val `data`: List<Topic>,
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
    val perpage: String,
    val readaccess: String,
    val saltkey: String,
    val success: String
)

data class Topic(
    val attachment: String,
    val author: String,
    val authorid: String,
    val bgcolor: String,
    val closed: String,
    val comments: String,
    val cover: String,
    val dateline: String,
    val dbdateline: String,
    val dblastpost: String,
    val digest: String,
    val displayorder: String,
    val favtimes: String,
    val fid: String,
    val folder: String,
    val heatlevel: String,
    val heats: String,
    val hidden: String,
    val highlight: String,
    val icon: String,
    val icontid: String,
    val id: String,
    val isgroup: String,
    val istoday: String,
    val lastpost: String,
    val lastposter: String,
    val lastposterenc: String,
    val maxposition: String,
    val moderated: String,
    val moved: String,
    val multipage: String,
    val new: String,
    val posttableid: String,
    val price: String,
    val pushedaid: String,
    val rate: String,
    val readperm: String,
    val recommend_add: String,
    val recommend_sub: String,
    val recommendicon: String,
    val recommends: String,
    val relatebytag: String,
    val replies: String,
    val replycredit: String,
    val rushreply: String,
    val sharetimes: String,
    val sortid: String,
    val special: String,
    val stamp: String,
    val status: String,
    val stickreply: String,
    val subject: String,
    val tid: String,
    val typeid: String,
    val views: String,
    val weeknew: String
)
