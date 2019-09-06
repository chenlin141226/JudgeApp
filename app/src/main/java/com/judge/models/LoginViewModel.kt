package com.judge.models

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.ImageView
import com.airbnb.mvrx.*
import com.judge.app.core.MvRxViewModel
import com.judge.data.bean.LoginBean
import com.judge.data.bean.LoginStatus
import com.judge.data.repository.LoginRepository
import com.judge.utils.LogUtils
import com.vondear.rxtool.RxSPTool
import com.vondear.rxtool.RxTool
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.support.v4.runOnUiThread

/**
 * @author: jaffa
 * @date: 2019/7/28
 * 登录界面的viewmodel
 */
data class LoginState(
    val loginRequest: Async<LoginBean> = Uninitialized,
    val login: LoginBean? = null,
    val isLoading: Boolean = false,
    val username: String = "",
    val password: String = "",
    val question: String? = null,
    val seccode: String = "",
    val codeUrl: Bitmap? = null,
    val loginStatus : LoginStatus? =null
) : MvRxState

class LoginViewModel(private val loginState: LoginState) : MvRxViewModel<LoginState>(loginState) {

    init {
        requestCode()
        isLogin()
    }

    //是否登录
    fun isLogin(){
        LoginRepository.isLogin().subscribeOn(Schedulers.io())
            .doOnSubscribe {
                setState { copy(isLoading = true) }
            }
            .doOnError {
                it.message?.let { it1 -> LogUtils.e(it1) }
            }
            .doFinally { setState { copy(isLoading = false) } }
            .execute { copy(loginStatus = it()?.Variables) }
     }

    //登录成功后会直接取上次的用户名和密码
    fun getUserNameAndPsw(){
        setState { copy(username = RxSPTool.getString(RxTool.getContext(),"username"),
            password = RxSPTool.getString(RxTool.getContext(),"password")) }
    }

    fun setUserName(username: String) {
        setState { copy(username = username) }
    }

    fun setPassword(password: String) {
        setState { copy(password = password) }
    }

    fun setCode(seccode: String) {
        setState { copy(seccode = seccode) }
    }

    fun setQuestion(question: String) {
        setState { copy(question = question) }
    }

    fun requestCode() = withState {
        LoginRepository.getCode().subscribeOn(Schedulers.io()).map {
            BitmapFactory.decodeStream(it.byteStream())
        }.execute { copy(codeUrl = it()) }

    }

    fun login() = withState { state ->

        val maps = hashMapOf("username" to state.username, "password" to state.password, "seccode" to state.seccode)
        LoginRepository.Login(maps).subscribeOn(Schedulers.io())
            .doOnSubscribe {
                setState { copy(isLoading = true) }
            }
            .doOnError {
                it.message?.let { it1 -> LogUtils.e(it1) }
            }
            .doFinally { setState { copy(isLoading = false) } }
            .execute {
                copy(loginRequest = it, login = it())
            }

    }


    fun reset(){
        setState { copy(loginRequest = Uninitialized,login = null) }
    }

    companion object : MvRxViewModelFactory<LoginViewModel, LoginState> {
        override fun create(viewModelContext: ViewModelContext, state: LoginState): LoginViewModel? {
            return LoginViewModel(state)
        }
    }
}