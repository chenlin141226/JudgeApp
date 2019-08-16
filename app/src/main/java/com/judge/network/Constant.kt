package com.judge.network

/**
 * @author: jaffa
 * @date: 2019/8/11
 */
object Constant {
     const val BASE_URL = "http://bbs.caipanshuo.com"

    //随机验证码
     const val SAFE_CODE = "/source/plugin/login_mobile/index.php?version=4&module=seccode"

    //手机验证码(post)
    const val PHONE_CODE = "/source/plugin/login_mobile/index.php?version=4&module=smscode&regist="

    //注册(post)
    const val REGISTER = "/source/plugin/login_mobile/index.php?version=4&module=regist"

    //登录
    const val LOGIN = "/source/plugin/login_mobile/index.php?version=4&module=login"

    //每日最佳，每周，每月
    const val JUDGE_INFORMATION = "/api/mobile/index.php"

    //裁判说 - 推荐

    const val RECOMMEND = "/api/mobile/index.php?version=4&module=get_diy&bid=100"
}