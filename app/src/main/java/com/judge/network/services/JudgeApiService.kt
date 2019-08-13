package com.judge.network.services

import com.judge.data.bean.InformationBean
import com.judge.network.Constant
import com.judge.network.JsonResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.QueryMap

/**
 * @author: jaffa
 * @date: 2019/8/12
 */
interface JudgeApiService {

    //每日，周，月
    @GET(Constant.JUDGE_INFORMATION)
    fun getInformation(@QueryMap map: HashMap<String, String>): Observable<JsonResponse<InformationBean>>
}