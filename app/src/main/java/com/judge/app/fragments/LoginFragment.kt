package com.judge.app.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast

import androidx.core.view.isVisible
import com.airbnb.mvrx.fragmentViewModel
import com.judge.R
import com.judge.app.activities.HomeActivity
import com.judge.app.core.BaseFragment
import com.judge.app.core.simpleController
import com.judge.data.state.LoginState
import com.judge.models.LoginViewModel
import com.judge.views.loginView
import com.vondear.rxtool.RxDataTool
import com.vondear.rxtool.view.RxToast
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun epoxyController() = simpleController(loginViewModel) { state ->
        loginView {
            id("login")
            //用户名
            userName(state.username)
            password(state.password)
            code(state.code)
            onUserNameChanged { loginViewModel.setUserName(it) }
            onPasswordChanged { loginViewModel.setPassword(it) }
            onCodeChanged { loginViewModel.setCode(it) }

            //跳过登录
            jumpClickListener { _ ->
                startActivity(Intent(context, HomeActivity::class.java))
                activity?.finish()
            }

            //下拉选择框的回调
            spinnerItemSelectedListener(object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                    val spinnerData = resources.getStringArray(R.array.login_item_spinner)
                    loginViewModel.setQuestion(spinnerData[position])
                }

            })

            //点击登录
            clickListener { _ ->
                loginViewModel.selectSubscribe(LoginState::username, LoginState::question) { username, question ->
                    context?.let { RxToast.info(it, question.toString(), Toast.LENGTH_SHORT, true).show() }
                    if (RxDataTool.isNullString(question)) {
                        toast("用户名不合法")
                    }
                }
            }

        }

    }

}