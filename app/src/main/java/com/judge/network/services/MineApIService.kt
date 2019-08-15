package com.judge.network.services

import com.judge.data.bean.MedalBean
import com.judge.data.bean.ProfileBean
import com.judge.data.bean.TopicBean
import com.judge.data.bean.UpLoadPhotoResultBean
import com.judge.network.JsonResponse
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.http.*

interface MineApIService {

    @GET("/api/mobile/index.php")
    fun getUerData(@QueryMap map: HashMap<String, String>): Observable<JsonResponse<ProfileBean>>

    @GET("/api/mobile/index.php")
    fun getMedals(@QueryMap map: HashMap<String, String>): Observable<JsonResponse<MedalBean>>

    @GET("/api/mobile/index.php")
    fun getWhistles(@QueryMap map: HashMap<String, String>): Observable<JsonResponse<MedalBean>>

    @GET("/api/mobile/index.php")
    fun getTopics(@QueryMap map: HashMap<String, String>): Observable<JsonResponse<TopicBean>>

    @Multipart
    @POST("/api/mobile/index.php")
    fun upLoadPhoto(@Part file: MultipartBody.Part,@QueryMap map: HashMap<String, String>): Observable<JsonResponse<UpLoadPhotoResultBean>>
}