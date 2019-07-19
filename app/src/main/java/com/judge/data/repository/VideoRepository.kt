package com.judge.data.repository

import com.judge.data.Video
import com.judge.network.ServiceCreator
import com.judge.network.services.VideoService
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class VideoRepository {
    private val videoService: VideoService by lazy {
        ServiceCreator.create(VideoService::class.java)
    }

    fun getVideos(): Observable<Video> {
        val map = HashMap<String, String>()
        map["timeStamp"] = "1559550233582"
        map["secretKey"] = "c9bf9964dd953dee4219801157dec08f"
        map["pageNum"] = "1"
        map["cate_id"] = "0"
        map["iUid"] = "0"
        return videoService.getVideos(map).subscribeOn(Schedulers.io()).map {
            it.data
        }
    }

}