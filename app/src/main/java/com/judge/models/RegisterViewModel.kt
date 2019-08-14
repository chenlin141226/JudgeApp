package com.judge.models

import android.widget.Toast
import com.airbnb.mvrx.*
import com.judge.app.core.MvRxViewModel
import com.judge.data.bean.RegisterResultBean
import com.judge.data.repository.LoginRepository
import com.vondear.rxtool.RxTool
import com.vondear.rxtool.view.RxToast
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * @author: jaffa
 * @date: 2019/7/28
 * 登录界面的viewmodel
 */
data class RegisterState(
    val sendResult: Async<RegisterResultBean> = Uninitialized,
    val register: RegisterResultBean? = null,
    val username: String = "",
    val password: String = "",
    val confirmPwd: String = "",
    val email: String = "",
    val phone: String = "",
    val phoneCode: String = "",
    val code: String = ""
) : MvRxState

class RegisterViewModel(registerState: RegisterState) :
    MvRxViewModel<RegisterState>(registerState) {


    fun setUserName(username: String) {
        setState { copy(username = username) }
    }

    fun setPassword(password: String) {
        setState { copy(password = password) }
    }

    fun setConfirmPwd(confirmPwd: String) {
        setState { copy(confirmPwd = confirmPwd) }
    }

    fun setEmail(email: String) {
        setState { copy(email = email) }
    }

    fun setPhoneNumber(phone: String) {
        // setState { copy(registerInfo = registerInfo?.copy(phone = phone)) }
        setState { copy(phone = phone) }
    }

    fun setPhoneCode(phoneCode: String) {
        setState { copy(phoneCode = phoneCode) }
    }

    fun setCode(code: String) {
        setState { copy(code = code) }
    }

    //获取手机验证码
    fun getPhoneCode() = withState { state ->
        if (state.phone.isEmpty()) {
            doAsync {
                if (state.phone.isEmpty()) {
                    uiThread {
                        RxToast.info(RxTool.getContext(), "请输入手机号码", Toast.LENGTH_SHORT, true).show()
                    }
                    return@doAsync
                }
            }
        } else {
            val map = hashMapOf("phone" to state.phone)

            LoginRepository.register(map)
                .subscribeOn(Schedulers.io())
                .execute {
                    copy(sendResult = it, register = it())
                }
        }

    }


    companion object : MvRxViewModelFactory<RegisterViewModel, RegisterState> {
        override fun create(viewModelContext: ViewModelContext, state: RegisterState): RegisterViewModel? {
            return RegisterViewModel(state)
        }
    }
}