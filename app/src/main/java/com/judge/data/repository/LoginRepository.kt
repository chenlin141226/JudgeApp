package com.judge.data.repository

import com.judge.data.bean.RegisterResultBean
import com.judge.network.JsonResponse
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

    fun register(map: HashMap<String,String>): Observable<RegisterResultBean> {
        return loginseivice.register(map)
    }
}