package com.judge.models

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.airbnb.mvrx.*
import com.judge.app.core.MvRxViewModel
import com.judge.data.bean.PhoneCodeBean
import com.judge.data.bean.RegisterResultBean
import com.judge.data.repository.LoginRepository
import io.reactivex.schedulers.Schedulers

/**
 * @author: jaffa
 * @date: 2019/7/28
 * 登录界面的viewmodel
 */
data class RegisterState(
    val sendResult: Async<PhoneCodeBean> = Uninitialized,
    val register: PhoneCodeBean? = null,
    val submitResult: Async<RegisterResultBean> = Uninitialized,
    val submit: RegisterResultBean? = null,
    val username: String = "",
    val password: String = "",
    val confirmPwd: String = "",
    val email: String = "",
    val phone: String = "",
    val phoneCode: String = "",//短信验证码
    val code: String = "",//随机验证码
    val codeUrl: Bitmap? = null
) : MvRxState

class RegisterViewModel(registerState: RegisterState) :
    MvRxViewModel<RegisterState>(registerState) {


    init {
        requestCode()
    }

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
        setState { copy(phone = phone) }
    }

    fun setPhoneCode(phoneCode: String) {
        setState { copy(phoneCode = phoneCode) }
    }

    fun setCode(code: String) {
        setState { copy(code = code) }
    }


    //注册
    fun submit() = withState { state ->
        val map = hashMapOf(
            "phone" to state.phone,
            "username" to state.username,
            "password" to state.password,
            "pcode" to state.phoneCode,
            "seccode" to state.code,
            "email" to state.email
        )

        LoginRepository.register(map).subscribeOn(Schedulers.io())
            .execute { copy(submitResult = it, submit = it()) }
    }

    //获取手机验证码
    fun getPhoneCode() = withState { state ->

        val map = hashMapOf("phone" to state.phone,"seccode" to state.code)

        LoginRepository.getPhoneCode(map)
            .subscribeOn(Schedulers.io())
            .execute {
                copy(sendResult = it, register = it())
            }
    }

    //获取随机验证码
    fun requestCode() = withState {
        LoginRepository.getCode().subscribeOn(Schedulers.io()).map {
            BitmapFactory.decodeStream(it.byteStream())
        }.execute { copy(codeUrl = it()) }

    }

    companion object : MvRxViewModelFactory<RegisterViewModel, RegisterState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: RegisterState
        ): RegisterViewModel? {
            return RegisterViewModel(state)
        }
    }
}