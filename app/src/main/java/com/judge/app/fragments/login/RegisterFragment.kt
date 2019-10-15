package com.judge.app.fragments.login

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.fragmentViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.judge.R
import com.judge.app.activities.LoggingActivity
import com.judge.app.core.BaseFragment
import com.judge.app.core.simpleController
import com.judge.data.repository.LoginRepository
import com.judge.models.RegisterState
import com.judge.models.RegisterViewModel
import com.judge.network.Constant
import com.judge.views.registView
import com.vondear.rxtool.RxConstTool
import com.vondear.rxtool.view.RxToast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.support.v4.runOnUiThread
import java.util.regex.Pattern

/**
 * @author: jaffa
 * @date: 2019/7/28
 * 注册界面
 */
class RegisterFragment : BaseFragment() {

    private val viewModel: RegisterViewModel by fragmentViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as LoggingActivity).tv_about_spannable.isVisible = true
        val navController = Navigation.findNavController(view)
        (activity as LoggingActivity).initView(navController)
    }

    override fun epoxyController() = simpleController(viewModel) { state ->


        registView {
            id("register")

            userName(state.username)

            password(state.password)

            confirmPassword(state.confirmPwd)

            email(state.email)

            phomeNumber(state.phone)

            messageCode(state.phoneCode)

            code(state.code)

            onUserNameChanged { viewModel.setUserName(it) }
            onPasswordChanged { viewModel.setPassword(it) }

            onConfirmPasswordChanged { viewModel.setConfirmPwd(it) }

            onEmailChanged { viewModel.setEmail(it) }

            onPhoneChanged { viewModel.setPhoneNumber(it) }

            onMessageChanged { viewModel.setPhoneCode(it) }

            onCodeChanged { viewModel.setCode(it) }

            //第一次获取验证码
            setimageBitmap(state.codeUrl)

            //返回按钮
            backClickListener { _ ->
                findNavController().navigateUp()
            }

            //随机验证码
            codeClickListener { _ ->
                viewModel.requestCode()
            }

            //发送验证码
            phoneCodeClickListener { _ ->
                if (state.phone.isEmpty()) {
                    context?.let {
                        RxToast.info(it,
                            resources.getString(R.string.hint_phone),
                            Toast.LENGTH_SHORT,
                            true).show()
                    }
                    return@phoneCodeClickListener
                }
                viewModel.getPhoneCode()
            }

            //            if (state.submitResult is Success && state.submit?.retcode == 1) {
            //                context?.let {
            //                    RxToast.info(
            //                        it,
            //                        state.submit.retmsg.toString(),
            //                        Toast.LENGTH_SHORT,
            //                        true
            //                    ).show()
            //                }
            //                findNavController().navigateUp()
            //            } else if (state.submit?.retcode == 100001) {
            //                context?.let {
            //                    RxToast.info(
            //                        it,
            //                        state.submit.retmsg.toString(),
            //                        Toast.LENGTH_SHORT,
            //                        true
            //                    ).show()
            //                }
            //            }
            //注册提交
            submitClickListener { _ ->

                if (markWords(state)) return@submitClickListener
                //viewModel.submit()
                val maps = hashMapOf("phone" to state.phone,
                    "username" to state.username,
                    "password" to state.password,
                    "pcode" to state.phoneCode,
                    "seccode" to state.code,
                    "email" to state.email)

                LoginRepository.register(maps).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe { it ->
                        val msg = it.retmsg
                        context?.let {
                            RxToast.info(it, msg.toString(), Toast.LENGTH_SHORT, true).show()
                        }
                        if (it.retcode == 1) {
                            findNavController().navigateUp()
                        }
                    }
            }
        }
    }

    private fun markWords(state: RegisterState): Boolean {
        if (state.username.isEmpty()) {
            Toast.makeText(context, R.string.hint_username, Toast.LENGTH_SHORT).show()
            return true
        }

        if (state.username.length < 3) {
            Toast.makeText(context, "用戶名不能少于三个字符", Toast.LENGTH_SHORT).show()
            return true
        }

        if (state.password.isEmpty()) {
            Toast.makeText(context, R.string.hint_password, Toast.LENGTH_SHORT).show()
            return true
        }

        if (state.password.length < 6) {
            Toast.makeText(context, "密码不能少于六个字符", Toast.LENGTH_SHORT).show()
            return true
        }

        if (state.confirmPwd.isEmpty()) {
            Toast.makeText(context, R.string.hint_confirm, Toast.LENGTH_SHORT).show()
            return true
        }

        if (state.confirmPwd != state.password) {
            Toast.makeText(context, R.string.isequal, Toast.LENGTH_SHORT).show()
            return true
        }
        if (state.email.isEmpty()) {
            Toast.makeText(context, R.string.hint_email, Toast.LENGTH_SHORT).show()
            return true
        }
        if (state.phone.isEmpty()) {
            Toast.makeText(context, R.string.hint_phone, Toast.LENGTH_SHORT).show()
            return true
        }

        if (!Pattern.compile(RxConstTool.REGEX_MOBILE_EXACT).matcher(state.phone).matches()) {
            Toast.makeText(context, "请输入正确的手机号码", Toast.LENGTH_SHORT).show()
            return true
        }

        if (state.phoneCode.isEmpty()) {
            Toast.makeText(context, R.string.hint_phoneCode, Toast.LENGTH_SHORT).show()
            return true
        }
        if (state.code.isEmpty()) {
            Toast.makeText(context, R.string.hint_code, Toast.LENGTH_SHORT).show()
            return true
        }
        return false
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.selectSubscribe(RegisterState::submit) { submit ->
            if (!submit?.retmsg.isNullOrEmpty()) {
                context?.let {
                    RxToast.info(it, submit?.retmsg.toString(), Toast.LENGTH_SHORT, true).show()
                }
            }
        }
    }

}