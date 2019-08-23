package com.judge.network.services

import com.judge.data.bean.*
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

    //推荐
    @GET(Constant.RECOMMEND)
    fun getRecommend(): Observable<JsonResponse<RecommendBean>>

    //主版
    @GET(Constant.EDITION)
    fun getEdition(): Observable<JsonResponse<EditionBean>>

    //关注
    @GET(Constant.ATTENTION)
    fun getAttention() : Observable<JsonResponse<AttentionBean>>

    //详情最新
    @GET(Constant.CATEGORYDETAIL)
    fun getNewCategoryDetail(@QueryMap map : HashMap<String,String>): Observable<JsonResponse<JudgeCategoryDetailBean>>

    //详情 热门
    @GET(Constant.Hot)
    fun getHotCategoryDetail(@QueryMap map : HashMap<String,String>): Observable<JsonResponse<JudgeCategoryDetailBean>>

    // 详情 热帖
    @GET(Constant.HOTTOPIC)
    fun getHotTopicCategoryDetail(@QueryMap map : HashMap<String,String>): Observable<JsonResponse<JudgeCategoryDetailBean>>

    //详情 金华
    //详情最新
    @GET(Constant.ESSENCE)
    fun getEssenceCategoryDetail(@QueryMap map : HashMap<String,String>): Observable<JsonResponse<JudgeCategoryDetailBean>>

    @GET(Constant.EXPRESSION)
    fun getExpression() : Observable<JsonResponse<EpressionBean>>
}