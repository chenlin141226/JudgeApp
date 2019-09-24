package com.judge.network.services

import com.judge.data.bean.BannerBean
import com.judge.data.bean.CommonResultBean
import com.judge.data.bean.NewsBean
import com.judge.data.bean.NewsDetailBean
import com.judge.network.JsonResponse
import io.reactivex.Observable
import retrofit2.http.*

interface HomeApIService {
    @GET("/api/mobile/index.php")
    fun getHomeBanner(@QueryMap map: HashMap<String, String>): Observable<JsonResponse<BannerBean>>

    @GET("/api/mobile/index.php")
    fun getHomeNews(@QueryMap map: HashMap<String, String>): Observable<JsonResponse<NewsBean>>

    @GET("/api/mobile/index.php")
    fun getNewsDetail(@QueryMap map: HashMap<String, String>): Observable<JsonResponse<NewsDetailBean>>

    @FormUrlEncoded
    @POST("/api/mobile/index.php?version=4&module=sendreply")
    fun sendNewsComment(@FieldMap map: HashMap<String, String>): Observable<JsonResponse<CommonResultBean>>

    @GET("/api/mobile/index.php")
    fun addToFavorite(@QueryMap map: HashMap<String, String>): Observable<JsonResponse<CommonResultBean>>

    @GET("/api/mobile/index.php")
    fun sendCommentToPerson(@QueryMap map: HashMap<String, String>): Observable<JsonResponse<CommonResultBean>>
}