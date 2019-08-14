package com.judge.models

import com.airbnb.mvrx.*
import com.judge.app.core.MvRxViewModel
import com.judge.data.bean.LoginBean
import com.judge.data.repository.LoginRepository
import io.reactivex.schedulers.Schedulers

/**
 * @author: jaffa
 * @date: 2019/7/28
 * 登录界面的viewmodel
 */
data class LoginState(
    val loginRequest : Async<LoginBean> = Uninitialized,
    val login : LoginBean? = null,
    val username : String = "",
    val password :  String = "",
    val question :  String? = null,
    val seccode : String = ""
): MvRxState

class LoginViewModel(private val  loginState: LoginState) : MvRxViewModel<LoginState>(loginState){

    init {

    }

    fun setUserName(username : String){
        setState { copy(username = username) }
    }

    fun setPassword(password : String){
        setState { copy(password = password) }
    }

    fun setCode(seccode : String){
        setState { copy(seccode = seccode) }
    }

    fun setQuestion(question : String){
        setState { copy(question = question) }
    }

    fun login() = withState { state: LoginState ->

        if (state.loginRequest is Loading) return@withState

        val maps = hashMapOf("username" to state.username,"password" to state.password,"seccode" to state.seccode)

        LoginRepository.Login(maps).subscribeOn(Schedulers.io())
            .execute {
                copy(loginRequest = it,login = it())
            }

    }


    companion object : MvRxViewModelFactory<LoginViewModel,LoginState>{
        override fun create(viewModelContext: ViewModelContext, state: LoginState): LoginViewModel? {
            return LoginViewModel(state)
        }
    }
}