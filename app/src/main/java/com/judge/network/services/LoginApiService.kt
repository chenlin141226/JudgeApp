package com.judge.network.services

import com.judge.data.bean.PhoneCodeBean
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

    //获取手机验证码
    @FormUrlEncoded
    @POST(Constant.PHONE_CODE)
    fun getPhoneCode(@FieldMap map: HashMap<String, String>): Observable<PhoneCodeBean>

    @FormUrlEncoded
    @POST(Constant.REGISTER)
    fun register(@FieldMap map: HashMap<String, String>): Observable<RegisterResultBean>
}