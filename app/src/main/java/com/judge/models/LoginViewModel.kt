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
        checkUserName1()
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

    fun setQuestion(question : String){
        setState { copy(question = question) }
    }

    fun checkUserName() : Boolean =  RxDataTool.isNullString(loginState.username)

    fun checkUserName1() = withState { loginState->
        val userisNull = RxDataTool.isNullString(loginState.username)
        if(userisNull){
            setState { copy(userNameIsOk = true) }
        }else{
            setState { copy(userNameIsOk = false) }
        }
    }


    companion object : MvRxViewModelFactory<LoginViewModel,LoginState>{
        override fun create(viewModelContext: ViewModelContext, state: LoginState): LoginViewModel? {
            val loginRepository = LoginRepository()
            return LoginViewModel(state,loginRepository)
        }
    }
}