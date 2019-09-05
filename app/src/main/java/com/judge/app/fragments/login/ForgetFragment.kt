package com.judge.app.fragments.login

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.airbnb.mvrx.MvRxState
import com.judge.app.activities.LoggingActivity
import com.judge.app.core.BaseFragment
import com.judge.app.core.simpleController
import com.judge.data.bean.LoginStatus
import com.judge.views.findPwdView
import com.vondear.rxtool.view.RxToast
import kotlinx.android.synthetic.main.activity_login.*

/**
 * @author: jaffa
 * @date: 2019/7/28
 * 找回密码Fragment
 */
data class FindPwdState(
    val loginStatus :LoginStatus,
    val isLoading: Boolean = false
) : MvRxState {

    constructor(args: LoginStatus) : this(loginStatus = args)
}

class ForgetFragment : BaseFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar.isVisible = false
        //隐藏部登录文字
        (activity as LoggingActivity).tv_about_spannable.isVisible = true
        val navController = Navigation.findNavController(view)
        (activity as LoggingActivity).initView(navController)
    }

    override fun epoxyController() = simpleController(){
        findPwdView {
             id("forget")

            onEmailChanged {  }

            onUserNameChanged {  }

            //返回按钮
            backCliclListener { _ ->
                findNavController().navigateUp()
            }

            //提交
            submitClickListener { _ ->
                context?.let { RxToast.info(it,"提交成功",Toast.LENGTH_SHORT,true).show() }
            }
        }
    }

    override fun setToolBar() {
        toolbar.visibility = View.INVISIBLE
    }
}