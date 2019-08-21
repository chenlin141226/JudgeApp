package com.judge.data.repository

import com.judge.data.bean.*
import com.judge.network.JsonResponse
import com.judge.network.ServiceCreator
import com.judge.network.services.JudgeApiService
import io.reactivex.Observable

/**
 * @author: jaffa
 * @date: 2019/8/12
 */
object JudgeRepository {

    private val judgeService: JudgeApiService by lazy {
        ServiceCreator.create(JudgeApiService::class.java)
    }

    //资讯
    fun getInformation(map: HashMap<String, String>): Observable<JsonResponse<InformationBean>> = judgeService.getInformation(map)

    //推荐
    fun getRecommend(): Observable<JsonResponse<RecommendBean>> = judgeService.getRecommend()

    //主版
    fun getEdition(): Observable<JsonResponse<EditionBean>> = judgeService.getEdition()

    //关注
    fun getAttention() : Observable<JsonResponse<AttentionBean>> = judgeService.getAttention()

    //详情（最新）
    fun getNewCategoryDetail(map: HashMap<String, String>) : Observable<JsonResponse<JudgeCategoryDetailBean>> = judgeService.getNewCategoryDetail(map)

    //详情（热门）
    fun getHotCategoryDetail(map: HashMap<String, String>) : Observable<JsonResponse<JudgeCategoryDetailBean>> = judgeService.getHotCategoryDetail(map)

    //详情（热帖）
    fun getHotTopicCategoryDetail(map: HashMap<String, String>) : Observable<JsonResponse<JudgeCategoryDetailBean>> = judgeService.getHotTopicCategoryDetail(map)

    //详情（精华）
    fun getEssenceCategoryDetail(map: HashMap<String, String>) : Observable<JsonResponse<JudgeCategoryDetailBean>> = judgeService.getEssenceCategoryDetail(map)
}