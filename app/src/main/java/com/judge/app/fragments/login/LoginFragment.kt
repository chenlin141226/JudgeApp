package com.judge.app.fragments.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.fragmentViewModel
import com.judge.R
import com.judge.app.activities.HomeActivity
import com.judge.app.activities.LoggingActivity
import com.judge.app.core.BaseFragment
import com.judge.app.core.simpleController
import com.judge.models.LoginViewModel
import com.judge.network.ServiceCreator
import com.judge.network.services.LoginApiService
import com.judge.views.loginView
import com.vondear.rxtool.RxSPTool
import com.vondear.rxtool.view.RxToast
import kotlinx.android.synthetic.main.activity_login.*


/**
 * @author: jaffa
 * @date: 2019/7/28
 * 登录Fragment
 */
class LoginFragment : BaseFragment() {

    val loginViewModel: LoginViewModel by fragmentViewModel()
    val loginseivice = ServiceCreator.create(LoginApiService::class.java)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        toolbar.isVisible = false
        (activity as LoggingActivity).tv_about_spannable.isVisible = false
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
            code(state.seccode)
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

            //第一次获取验证码
            setimageBitmap(state.codeUrl)

            //点击更新验证码
            codeClickListener { _ ->
                loginViewModel.requestCode()
            }

            //点击登录
            clickListener { _ ->
                if (state.username.isEmpty()) {
                    context?.let {
                        RxToast.info(
                            it,
                            resources.getString(R.string.hint_username),
                            Toast.LENGTH_SHORT,
                            false
                        ).show()
                    }
                    return@clickListener
                }

                if (state.password.isEmpty()) {
                    context?.let {
                        RxToast.info(
                            it,
                            resources.getString(R.string.hint_password),
                            Toast.LENGTH_SHORT,
                            false
                        ).show()
                    }
                    return@clickListener
                }

                if (state.seccode.isEmpty()) {
                    context?.let {
                        RxToast.info(it, resources.getString(R.string.hint_code), Toast.LENGTH_SHORT, false).show()
                    }
                    return@clickListener
                }

                loginViewModel.login()
            }

            if (state.loginRequest is Success && state.login?.retcode==0) {
                //保存用户名密码
                RxSPTool.putString(context,"username",state.username)
                RxSPTool.putString(context,"password",state.password)
                context?.let {
                    RxToast.info(it, state.login.retmsg.toString(), Toast.LENGTH_SHORT, false).show()
                }
                startActivity(Intent(context, HomeActivity::class.java))
                activity?.finish()
            }else if(state.login?.retcode== 100001){
                context?.let {
                    RxToast.info(it, state.login.retmsg.toString(), Toast.LENGTH_SHORT, false).show()
                }
                loginViewModel.reset()
            }


            //立即注册
            registerClickListener { _ ->
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }
            //找回密码
            findClickListener { _ ->
              navigateTo(R.id.action_homeFragment_to_forgetFragment,state.loginStatus)
            }

        }

    }

    override fun initData() {
        loginViewModel.isLogin()
        loginViewModel.getUserNameAndPsw()
    }
}