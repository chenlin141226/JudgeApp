package com.judge.network.services

import com.judge.data.bean.BannerBean
import com.judge.data.bean.NewsBean
import com.judge.network.JsonResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface HomeApIService {
    @GET("/api/mobile/index.php")
    fun getHomeBanner(@QueryMap map: HashMap<String, String>): Observable<JsonResponse<BannerBean>>

    @GET("/api/mobile/index.php")
    fun getHomeNews(@QueryMap map: HashMap<String, String>): Observable<JsonResponse<NewsBean>>
}