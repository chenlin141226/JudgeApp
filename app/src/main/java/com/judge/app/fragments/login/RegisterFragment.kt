package com.judge.app.fragments.login

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.airbnb.mvrx.fragmentViewModel
import com.judge.app.activities.LoggingActivity
import com.judge.app.core.BaseFragment
import com.judge.app.core.simpleController
import com.judge.models.RegisterState
import com.judge.models.RegisterViewModel
import com.judge.views.registView
import com.vondear.rxtool.view.RxToast
import kotlinx.android.synthetic.main.activity_login.*

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

            backClickListener { _ ->
                findNavController().navigateUp()
            }

            codeClickListener { _ ->
                context?.let { RxToast.info(it, "Jaffa", Toast.LENGTH_SHORT, true).show() }
            }

            //发送验证码
            phonrCodeClickListener { _ ->

            }

            //注册提交
            submitClickListener { _ ->
                context?.let { RxToast.info(it, state.phone, Toast.LENGTH_SHORT, true).show() }
                viewModel.selectSubscribe(RegisterState::phone){phone ->
                    context?.let { RxToast.info(it, phone, Toast.LENGTH_SHORT, true).show() }
                }

         }
        }
    }

    override fun initData() {

    }

}