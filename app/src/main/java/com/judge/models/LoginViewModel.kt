package com.judge.models

import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.judge.app.core.MvRxViewModel
import com.judge.data.repository.LoginRepository
import com.judge.data.state.LoginState

/**
 * @author: jaffa
 * @date: 2019/7/28
 */
class LoginViewModel(loginState: LoginState,private val loginRepository: LoginRepository) : MvRxViewModel<LoginState>(loginState){

    init {

    }

    companion object : MvRxViewModelFactory<LoginViewModel,LoginState>{
        override fun create(viewModelContext: ViewModelContext, state: LoginState): LoginViewModel? {
            val loginRepository = LoginRepository()
            return LoginViewModel(state,loginRepository)
        }
    }
}