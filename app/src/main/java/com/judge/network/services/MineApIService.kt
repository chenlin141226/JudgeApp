package com.judge.network.services

import com.judge.data.bean.*
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
    fun getTopics(@QueryMap map: HashMap<String, String>): Observable<JsonResponse<TopicBean>>

    @Multipart
    @POST("/api/mobile/index.php")
    fun upLoadPhoto(
        @Part file: MultipartBody.Part,
        @QueryMap map: HashMap<String, String>
    ): Observable<JsonResponse<UpLoadPhotoResultBean>>

    @GET("/api/mobile/index.php")
    fun getPublicMessage(@QueryMap map: HashMap<String, String>): Observable<JsonResponse<PublicMessageBean>>

    @GET("/api/mobile/index.php")
    fun getPersonalMessage(@QueryMap map: HashMap<String, String>): Observable<JsonResponse<PersonalMessageBean>>

    @GET("/api/mobile/index.php")
    fun getFriends(@QueryMap map: HashMap<String, String>): Observable<JsonResponse<FriendBean>>

    @GET("/api/mobile/index.php")
    fun getFriendsMessage(@QueryMap map: HashMap<String, String>): Observable<JsonResponse<FriendsMessageBean>>

    @FormUrlEncoded
    @POST("/api/mobile/index.php?version=4&module=sendpm")
    fun sendMessage(@FieldMap map: HashMap<String, String>): Observable<JsonResponse<CommonResultBean>>

    @FormUrlEncoded
    @POST("/api/mobile/index.php?version=4&module=zen_update_profile")
    fun updateProfile(@FieldMap map: HashMap<String, String>): Observable<JsonResponse<ProfileUpdateResultBean>>

    @FormUrlEncoded
    @POST("/api/mobile/index.php")
    fun addFriend(@QueryMap queryMap: HashMap<String, String>, @FieldMap map: HashMap<String, String>): Observable<JsonResponse<CommonResultBean>>

    @FormUrlEncoded
    @POST("/api/mobile/index.php")
    fun deleteTopics(
        @QueryMap queryMap: HashMap<String, String>,
        @FieldMap fieldMap: HashMap<String, String>,
        @Field("favorite[]") ids: List<String>
    ): Observable<JsonResponse<CommonResultBean>>
}