package com.judge.data.repository

import com.judge.data.bean.LoginBean
import com.judge.data.bean.PhoneCodeBean
import com.judge.data.bean.RegisterResultBean
import com.judge.network.ServiceCreator
import com.judge.network.services.LoginApiService
import io.reactivex.Observable
import okhttp3.ResponseBody

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
    fun getPhoneCode(map: HashMap<String,String>): Observable<PhoneCodeBean> = loginseivice.getPhoneCode(map)

    fun getCode() : Observable<ResponseBody> = loginseivice.getCode()

    //注册
    fun register(map : HashMap<String,String>) : Observable<RegisterResultBean> = loginseivice.register(map)

    //登录
    fun Login(map : HashMap<String,String>) : Observable<LoginBean> =loginseivice.login(map)
}