package com.judge.app.fragments.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.Toast

import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.airbnb.mvrx.fragmentViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.judge.R
import com.judge.app.activities.HomeActivity
import com.judge.app.activities.LoggingActivity
import com.judge.app.core.BaseFragment
import com.judge.app.core.simpleController
import com.judge.models.LoginState
import com.judge.models.LoginViewModel
import com.judge.network.Constant
import com.judge.views.loginView
import com.vondear.rxtool.view.RxToast
import kotlinx.android.synthetic.main.activity_login.*


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

            //点击更新验证码
            codeClickListener { btn_code ->
                Glide.with(context!!)
                    .load(Constant.BASE_URL + Constant.SAFE_CODE)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .centerCrop()
                    .into(btn_code as ImageView)
            }

            //点击登录
            clickListener { _ ->
                if (state.username.isEmpty()) {
                    context?.let { RxToast.info(it, resources.getString(R.string.hint_username), Toast.LENGTH_SHORT, false).show() }
                    return@clickListener
                }

                if (state.password.isEmpty()) {
                    context?.let { RxToast.info(it, resources.getString(R.string.hint_password), Toast.LENGTH_SHORT, false).show() }
                    return@clickListener
                }

                if (state.seccode.isEmpty()) {
                    context?.let { RxToast.info(it, resources.getString(R.string.hint_code), Toast.LENGTH_SHORT, false).show() }
                    return@clickListener
                }

                loginViewModel.login()

                if(state.login?.retcode == 100001){
                    context?.let { RxToast.info(it, state.login.retmsg.toString(), Toast.LENGTH_SHORT, false).show() }
                }
            }



            //立即注册
            registerClickListener { _ ->
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }
            //找回密码
            findClickListener { _ ->
                findNavController().navigate(R.id.action_homeFragment_to_forgetFragment)
            }

        }

    }

}