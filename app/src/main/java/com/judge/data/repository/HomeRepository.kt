package com.judge.data.repository

import com.judge.data.bean.BannerBean
import com.judge.data.bean.CommonResultBean
import com.judge.data.bean.NewsBean
import com.judge.data.bean.NewsDetailBean
import com.judge.network.JsonResponse
import com.judge.network.ServiceCreator
import com.judge.network.services.HomeApIService
import io.reactivex.Observable

object HomeRepository {
    private val homeService: HomeApIService by lazy {
        ServiceCreator.create(HomeApIService::class.java)
    }

    fun fetchBanners(map: HashMap<String, String>): Observable<JsonResponse<BannerBean>> {
        return homeService.getHomeBanner(map)
    }

    fun fetchNews(map: HashMap<String, String>): Observable<JsonResponse<NewsBean>> {
        return homeService.getHomeNews(map)
    }

    fun fetchNewsDetail(map: HashMap<String, String>): Observable<JsonResponse<NewsDetailBean>> {
        return homeService.getNewsDetail(map)
    }

    fun sendNewsComment(map: HashMap<String, String>): Observable<JsonResponse<CommonResultBean>> {
        return homeService.sendNewsComment(map)
    }

    fun addToFavorite(map: HashMap<String, String>): Observable<JsonResponse<CommonResultBean>> {
        return homeService.addToFavorite(map)
    }

    fun sendCommentToPerson(map: HashMap<String, String>): Observable<JsonResponse<CommonResultBean>> {
        return homeService.sendCommentToPerson(map)
    }
}