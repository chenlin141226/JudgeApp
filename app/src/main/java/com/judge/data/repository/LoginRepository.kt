package com.judge.data.repository

import com.judge.data.bean.LoginBean
import com.judge.data.bean.PhoneCodeBean
import com.judge.data.bean.RegisterResultBean
import com.judge.network.ServiceCreator
import com.judge.network.services.LoginApiService
import io.reactivex.Observable

/**
 * @author: jaffa
 * @date: 2019/7/28
 * 登录界面的respository
 */
object LoginRepository {
    private val loginseivice: LoginApiService by lazy {
        ServiceCreator.create(LoginApiService::class.java)
    }

    //获取手机验证码
    fun getPhoneCode(map: HashMap<String,String>): Observable<PhoneCodeBean> {
        return loginseivice.getPhoneCode(map)
    }

    //注册
    fun register(map : HashMap<String,String>) : Observable<RegisterResultBean>{
        return  loginseivice.register(map)
    }

    //登录
    fun Login(username:String,password:String,secode:String) : Observable<LoginBean>{
        return loginseivice.login(username,password,secode)
    }
}