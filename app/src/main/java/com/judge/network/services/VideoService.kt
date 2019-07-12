package com.judge.network.services

import com.judge.data.Video
import com.judge.network.JsonResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface VideoService {

    @GET("/v1/wapVideo")
    fun getVideos(@QueryMap map: HashMap<String, String>): Observable<JsonResponse<Video>>

}