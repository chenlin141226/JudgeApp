package com.judge.data.repository

import com.judge.data.bean.MedalBean
import com.judge.data.bean.MineDataBean
import com.judge.network.JsonResponse
import com.judge.network.ServiceCreator
import com.judge.network.services.MineApIService
import io.reactivex.Observable

object MineRepository {
    private val mineService: MineApIService by lazy {
        ServiceCreator.create(MineApIService::class.java)
    }

    fun getUserData(map: HashMap<String, String>): Observable<JsonResponse<MineDataBean>> {
        return mineService.getUerData(map)
    }

    fun getMedals(map: HashMap<String, String>): Observable<JsonResponse<MedalBean>> {
        return mineService.getMedals(map)
    }
}