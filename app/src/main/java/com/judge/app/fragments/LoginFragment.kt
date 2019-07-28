package com.judge.app.fragments

import com.airbnb.mvrx.fragmentViewModel
import com.judge.app.core.BaseFragment
import com.judge.app.core.simpleController
import com.judge.models.LoginViewModel
import com.judge.views.loginView

/**
 * @author: jaffa
 * @date: 2019/7/28
 * 登录Fragment
 */
class LoginFragment : BaseFragment() {

    val loginViewModel: LoginViewModel by fragmentViewModel()

    override fun epoxyController() = simpleController(loginViewModel) { state ->
        loginView {
            id("login")
        }
    }
}