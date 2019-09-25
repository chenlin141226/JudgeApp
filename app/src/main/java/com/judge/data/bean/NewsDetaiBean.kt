package com.judge.data.bean

data class NewsDetailBean(
    val allowpostcomment: List<String>,
    val auth: String,
    val cache_custominfo_postno: List<String>,
    val code: String,
    val commentcount: List<Any>,
    val comments: List<Any>,
    val cookiepre: String,
    val fid: String,
    val fid_name: String,
    val formhash: String,
    val forum: Forum,
    val forum_threadpay: String,
    val groupid: String,
    val isfav: String,
    val ismoderator: String,
    val member_avatar: String,
    val member_uid: String,
    val member_username: String,
    val notice: Notice,
    val postlist: List<Postlist>,
    val ppp: String,
    val readaccess: String,
    val saltkey: String,
    val setting_rewriterule: SettingRewriterule,
    val setting_rewritestatus: List<Any>,
    val success: String,
    val thread: Thread,
    val favid: String
)

data class Postlist(
    val adminid: String,
    val anonymous: String,
    val attachment: String,
    val author: String,
    val authorid: String,
    val dateline: String,
    val dbdateline: String,
    val first: String,
    val groupiconid: String,
    val groupid: String,
    val leavel: String,
    val memberstatus: String,
    val message: String,
    val number: String,
    val pid: String,
    val position: String,
    val recovery_time: String,
    val replycredit: String,
    val status: String,
    val tid: String,
    val touxiang: String,
    val username: String
)

data class SettingRewriterule(
    val forum_archiver: String,
    val forum_forumdisplay: String,
    val forum_viewthread: String,
    val group_group: String,
    val home_blog: String,
    val home_space: String,
    val plugin: String,
    val portal_article: String,
    val portal_topic: String
)

data class Thread(
    val allreplies: String,
    val archiveid: String,
    val attachment: String,
    val author: String,
    val authorid: String,
    val bgcolor: String,
    val closed: String,
    val comments: String,
    val cover: String,
    val dateline: String,
    val digest: String,
    val displayorder: String,
    val favtimes: String,
    val fid: String,
    val heatlevel: String,
    val heats: String,
    val hidden: String,
    val highlight: String,
    val icon: String,
    val is_archived: String,
    val isgroup: String,
    val lastpost: String,
    val lastposter: String,
    val maxposition: String,
    val moderated: String,
    val ordertype: String,
    val posttable: String,
    val posttableid: String,
    val price: String,
    val pushedaid: String,
    val rate: String,
    val readperm: String,
    val recommend: String,
    val recommend_add: String,
    val recommend_sub: String,
    val recommendlevel: String,
    val recommends: String,
    val relatebytag: String,
    val relay: String,
    val replies: String,
    val replycredit: String,
    val replycredit_rule: ReplycreditRule,
    val sharetimes: String,
    val short_subject: String,
    val sortid: String,
    val special: String,
    val stamp: String,
    val status: String,
    val stickreply: String,
    val subject: String,
    val subjectenc: String,
    val threadtable: String,
    val threadtableid: String,
    val tid: String,
    val typeid: String,
    val views: String
)

data class ReplycreditRule(
    val extcreditstype: String
)