package com.judge.data.bean

/**
 * @author: jaffa
 * @date: 2019/8/19
 */
data class JudgeCategoryDetailBean(
    val auth: String,
    val cookiepre: String,
    val formhash: String,
    val forum: Forum,
    val forum_threadlist: List<ForumThreadlist>,
    val group: Group,
    val groupid: String,
    val ismoderator: String,
    val member_avatar: String,
    val member_uid: String,
    val member_username: String,
    val notice: Notice,
    val page: String,
    val readaccess: String,
    val reward_unit: String,
    val saltkey: String,
    val sublist: List<Any>,
    val tpp: String
)

data class Forum(
    val autoclose: String,
    val description: String,
    val fid: String,
    val fup: String,
    val icon: String,
    val name: String,
    val password: String,
    val picstyle: String,
    val posts: String,
    val rules: String,
    val threadcount: String,
    val threads: String,
    val todayposts: String
)


data class ForumThreadlist(
    val attachment: String,
    val author: String,
    val authorid: String,
    val dateline: String,
    val dbdateline: String,
    val dblastpost: String,
    val digest: String,
    val displayorder: String,
    val lastpost: String,
    val lastposter: String,
    val price: String,
    val readperm: String,
    val recommend: String,
    val recommend_add: String,
    val replies: String,
    val reply: List<Reply>,
    val replycredit: String,
    val rushreply: String,
    val special: String,
    val subject: String,
    val tid: String,
    val typeid: String,
    val views: String
)

data class Reply(
    val author: String,
    val authorid: String,
    val message: String,
    val pid: String
)

