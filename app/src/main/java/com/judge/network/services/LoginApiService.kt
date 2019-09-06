package com.judge.network.services

import com.judge.data.bean.*
import com.judge.network.Constant
import com.judge.network.JsonResponse
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.*

/**
 * @author: jaffa
 * @date: 2019/8/12
 */
interface LoginApiService {

    //获取手机验证码
    @FormUrlEncoded
    @POST(Constant.PHONE_CODE)
    fun getPhoneCode(@FieldMap map: HashMap<String, String>): Observable<PhoneCodeBean>

    //注册
    @FormUrlEncoded
    @POST(Constant.REGISTER)
    fun register(@FieldMap map: HashMap<String, String>): Observable<RegisterResultBean>

    //登录
    @FormUrlEncoded
    @POST(Constant.LOGIN)
    fun login(@FieldMap map : HashMap<String,String>) : Observable<LoginBean>

    //获取随机验证码
    @GET(Constant.SAFE_CODE)
    fun getCode() : Observable<ResponseBody>

    //是否登录
    @GET(Constant.LOGINSTATUS)
    fun isLogin() : Observable<JsonResponse<LoginStatus>>

    //找回密码
    @FormUrlEncoded
    @POST(Constant.FINDPASSWORD)
    fun findPwd(@FieldMap map: HashMap<String, String>) : Observable<JsonResponse<FindPwdBean>>
}