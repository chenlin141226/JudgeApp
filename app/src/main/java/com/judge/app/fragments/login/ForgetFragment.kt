package com.judge.app.fragments.login

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.airbnb.mvrx.fragmentViewModel
import com.judge.app.activities.LoggingActivity
import com.judge.app.core.BaseFragment
import com.judge.app.core.MvRxViewModel
import com.judge.app.core.simpleController
import com.judge.data.bean.FindPwdBean
import com.judge.data.bean.LoginStatus
import com.judge.data.repository.LoginRepository
import com.judge.network.Message
import com.judge.utils.LogUtils
import com.judge.views.findPwdView
import com.vondear.rxtool.view.RxToast
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*

/**
 * @author: jaffa
 * @date: 2019/7/28
 * 找回密码Fragment
 */
data class FindPwdState(
    val loginStatus: LoginStatus,
    val isLoading: Boolean = false,
    val userName: String = "",
    val email: String = "",
    val findPwd: FindPwdBean? = null,
    val message: Message? = null
) : MvRxState {

    constructor(args: LoginStatus) : this(loginStatus = args)
}

class FindPwdViewModel(initialiState: FindPwdState) : MvRxViewModel<FindPwdState>(initialiState) {


    fun setUserName(username: String) {
        setState { copy(userName = username) }
    }

    fun setEmail(email: String) {
        setState { copy(email = email) }
    }

    fun findPwd() = withState { state ->
        val map = hashMapOf(
            "formhash" to state.loginStatus.formhash,
            "email" to state.email,
            "username" to state.userName
        )
        LoginRepository.findPwd(map).subscribeOn(Schedulers.io())
            .doOnSubscribe {
                setState { copy(isLoading = true) }
            }
            .doOnError {
                it.message?.let { it1 -> LogUtils.e(it1) }
            }
            .doFinally { setState { copy(isLoading = false) } }
            .execute {
                copy(findPwd = it()?.Variables,
                message = it()?.Message) }
    }

    fun reset(){
        setState { copy(findPwd = null,message = null) }
    }

    companion object : MvRxViewModelFactory<FindPwdViewModel, FindPwdState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: FindPwdState
        ): FindPwdViewModel? {
            return FindPwdViewModel(state)
        }
    }
}

class ForgetFragment : BaseFragment() {

    val viewModel: FindPwdViewModel by fragmentViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar.isVisible = false
        //隐藏部登录文字
        (activity as LoggingActivity).tv_about_spannable.isVisible = true
        val navController = Navigation.findNavController(view)
        (activity as LoggingActivity).initView(navController)
    }

    override fun epoxyController() = simpleController(viewModel) { state ->
        findPwdView {
            id("forget")

            email(state.email)

            userName(state.userName)

            onEmailChanged { viewModel.setEmail(it) }

            onUserNameChanged { viewModel.setUserName(it) }

            //返回按钮
            backCliclListener { _ ->
                findNavController().navigateUp()
            }

            //提交
            submitClickListener { _ ->
                viewModel.findPwd()
            }

            if(state.findPwd?.code == "1"){
                context?.let { RxToast.info(it, state.message?.messagestr.toString(), Toast.LENGTH_SHORT, true).show() }
            }else if(state.findPwd?.code == "0"){
                context?.let { RxToast.info(it, state.message?.messagestr.toString(), Toast.LENGTH_SHORT, true).show() }
            }
        }
    }

    override fun setToolBar() {
        toolbar.visibility = View.INVISIBLE
    }

    override fun onDestroyView() {
        viewModel.reset()
        super.onDestroyView()
    }
}