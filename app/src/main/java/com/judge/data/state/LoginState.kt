package com.judge.data.state

import com.airbnb.mvrx.MvRxState

/**
 * @author: jaffa
 * @date: 2019/7/28
 */
data class LoginState(
 val username : String? = null,
 val password :  String? = null,
 val question :  String? = null,
 val userNameIsOk : Boolean = true,
 val code : String? = null
): MvRxState