package com.judge.models

import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.judge.app.core.MvRxViewModel
import com.judge.data.repository.LoginRepository
import com.judge.data.state.LoginState
import com.vondear.rxtool.RxDataTool

/**
 * @author: jaffa
 * @date: 2019/7/28
 * 登录界面的viewmodel
 */
class LoginViewModel(private val  loginState: LoginState,private val loginRepository: LoginRepository) : MvRxViewModel<LoginState>(loginState){

    init {

    }

    fun setUserName(username : String){
        setState { copy(username = username) }
    }

    fun setPassword(password : String){
        setState { copy(password = password) }
    }

    fun setCode(code : String){
        setState { copy(code = code) }
    }

    fun checkUserName():Boolean = RxDataTool.isEmpty(loginState.username)

    companion object : MvRxViewModelFactory<LoginViewModel,LoginState>{
        override fun create(viewModelContext: ViewModelContext, state: LoginState): LoginViewModel? {
            val loginRepository = LoginRepository()
            return LoginViewModel(state,loginRepository)
        }
    }
}