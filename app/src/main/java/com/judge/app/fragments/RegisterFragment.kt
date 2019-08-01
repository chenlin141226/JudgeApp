package com.judge.app.fragments

import android.os.Bundle
import android.view.View
import androidx.navigation.Navigation
import com.judge.app.activities.LoggingActivity
import com.judge.app.core.BaseFragment
import com.judge.app.core.simpleController
import com.judge.views.registView

/**
 * @author: jaffa
 * @date: 2019/7/28
 * 注册界面
 */
class RegisterFragment : BaseFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = Navigation.findNavController(view)
        (activity as LoggingActivity).initView(navController)
    }

    override fun epoxyController() = simpleController(){
        registView {
            id("register")
        }
    }
}