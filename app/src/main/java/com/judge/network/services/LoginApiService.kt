package com.judge.network.services

import com.judge.data.bean.RegisterResultBean
import com.judge.network.Constant
import io.reactivex.Observable
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.QueryMap

/**
 * @author: jaffa
 * @date: 2019/8/12
 */
interface LoginApiService {

    //注册
    @FormUrlEncoded
    @POST(Constant.PHONE_CODE)
    fun register(@FieldMap map: HashMap<String, String>): Observable<RegisterResultBean>
}