package com.judge.network.services

import com.judge.data.MedalBean
import com.judge.data.MineDataBean
import com.judge.network.JsonResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface MineApIService {

    @GET("/api/mobile/index.php")
    fun getUerData(@QueryMap map: HashMap<String, String>): Observable<JsonResponse<MineDataBean>>

    @GET("/api/mobile/index.php")
    fun getMedals(@QueryMap map: HashMap<String, String>): Observable<JsonResponse<MedalBean>>
}