package com.judge.app.fragments.login

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
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
import com.judge.data.bean.LoginBean
import com.judge.data.repository.LoginRepository
import com.judge.models.LoginState
import com.judge.models.LoginViewModel
import com.judge.network.Constant
import com.judge.network.ServiceCreator
import com.judge.network.services.LoginApiService
import com.judge.views.loginView
import com.vondear.rxtool.view.RxToast
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.support.v4.runOnUiThread


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

            //点击更新验证码
            codeClickListener { btn_code ->


                loginseivice.getCode().subscribeOn(Schedulers.io()).map {
                    BitmapFactory.decodeStream(it.byteStream())
                }.subscribe {
                    runOnUiThread {
                        (btn_code as ImageView).setImageBitmap(it)
                    }
                }

//                Glide.with(context!!)
//                    .load(Constant.BASE_URL + Constant.SAFE_CODE)
//                    .diskCacheStrategy(DiskCacheStrategy.NONE)
//                    .skipMemoryCache(true)
//                    .centerCrop()
//                    .into(btn_code as ImageView)
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

                if (state.login?.retmsg == "0") {
                    context?.let {
                        RxToast.info(it, state.login.retmsg.toString(), Toast.LENGTH_SHORT, false).show()
                    }
                } else {
                    context?.let {
                        RxToast.info(it, state.login?.retmsg.toString(), Toast.LENGTH_SHORT, false).show()
                    }
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