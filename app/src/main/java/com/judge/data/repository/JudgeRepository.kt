package com.judge.data.repository

import com.judge.data.bean.EditionBean
import com.judge.data.bean.InformationBean
import com.judge.data.bean.RecommendBean
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

    fun getInformation(map: HashMap<String, String>): Observable<JsonResponse<InformationBean>> = judgeService.getInformation(map)

    fun getRecommend(): Observable<JsonResponse<RecommendBean>> = judgeService.getRecommend()

    fun getEdition(): Observable<JsonResponse<EditionBean>> = judgeService.getEdition()
}