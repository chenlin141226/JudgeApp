package com.judge.app.fragments.login

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.judge.app.activities.LoggingActivity
import com.judge.app.core.BaseFragment
import com.judge.app.core.simpleController
import com.judge.views.registView
import com.vondear.rxtool.view.RxToast
import kotlinx.android.synthetic.main.activity_login.*

/**
 * @author: jaffa
 * @date: 2019/7/28
 * 注册界面
 */
class RegisterFragment : BaseFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as LoggingActivity).tv_about_spannable.isVisible = true
        val navController = Navigation.findNavController(view)
        (activity as LoggingActivity).initView(navController)
    }

    override fun epoxyController() = simpleController(){
        registView {
            id("register")

            onUserNameChanged {  }

            onPasswordChanged {  }

            onConfirmPasswordChanged {  }

            onEmailChanged {  }

            onPhoneChanged {  }

            onMessageChanged {  }

            onCodeChanged {  }

            backClickListener { _->
                findNavController().navigateUp()
            }

            codeClickListener { _->
                context?.let { RxToast.info(it, "Jaffa", Toast.LENGTH_SHORT, true).show() }
            }

            submitClickListener {_->
                context?.let { RxToast.info(it, "Jaffa", Toast.LENGTH_SHORT, true).show() }
            }
        }
    }
}