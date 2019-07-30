package com.judge.models

import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.judge.app.core.MvRxViewModel
import com.judge.data.repository.LoginRepository
import com.judge.data.state.LoginState

/**
 * @author: jaffa
 * @date: 2019/7/28
 * 登录界面的viewmodel
 */
class LoginViewModel(loginState: LoginState,private val loginRepository: LoginRepository) : MvRxViewModel<LoginState>(loginState){

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

    fun checkUserName()  = withState {state ->
       if(state.username.isNullOrEmpty ()){
//           setState {
//               copy(userNameIsOk = username.isEmpty{})
//           }
       }
        //setState { copy(userNameIsOk = s) }
    }

    companion object : MvRxViewModelFactory<LoginViewModel,LoginState>{
        override fun create(viewModelContext: ViewModelContext, state: LoginState): LoginViewModel? {
            val loginRepository = LoginRepository()
            return LoginViewModel(state,loginRepository)
        }
    }
}