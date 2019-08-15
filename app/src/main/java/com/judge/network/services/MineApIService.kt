package com.judge.network.services

import com.judge.data.bean.MedalBean
import com.judge.data.bean.MineDataBean
import com.judge.data.bean.TopicBean
import com.judge.network.JsonResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface MineApIService {

    @GET("/api/mobile/index.php")
    fun getUerData(@QueryMap map: HashMap<String, String>): Observable<JsonResponse<MineDataBean>>

    @GET("/api/mobile/index.php")
    fun getMedals(@QueryMap map: HashMap<String, String>): Observable<JsonResponse<MedalBean>>

    @GET("/api/mobile/index.php")
    fun getWhistles(@QueryMap map: HashMap<String, String>): Observable<JsonResponse<MedalBean>>

    @GET("/api/mobile/index.php")
    fun getTopics(@QueryMap map: HashMap<String, String>): Observable<JsonResponse<TopicBean>>
}