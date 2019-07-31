package com.judge.app.fragments

import android.os.Bundle

import androidx.core.view.isVisible
import com.airbnb.mvrx.fragmentViewModel
import com.judge.app.core.BaseFragment
import com.judge.app.core.simpleController
import com.judge.models.LoginViewModel
import com.judge.views.loginView
import org.jetbrains.anko.support.v4.toast

/**
 * @author: jaffa
 * @date: 2019/7/28
 * 登录Fragment
 */
class LoginFragment : BaseFragment() {

    val loginViewModel: LoginViewModel by fragmentViewModel()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        toolbar.isVisible = false
    }

    override fun epoxyController() = simpleController(loginViewModel) { state ->
        loginView {
            id("login")
            userName(state.username)
            password(state.password)
            code(state.code)
            onUserNameChanged { loginViewModel.setUserName(it) }
            onPasswordChanged { loginViewModel.setPassword(it) }
            onCodeChanged { loginViewModel.setCode(it) }
            clickListener { _->
                if(loginViewModel.checkUserName()){
                    toast("null")
                }
            }
        }
    }
}