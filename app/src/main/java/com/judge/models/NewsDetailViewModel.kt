package com.judge.models

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import com.judge.app.core.MvRxViewModel
import com.judge.data.bean.CommonResultBean
import com.judge.data.bean.News
import com.judge.data.bean.NewsDetailBean
import com.judge.data.bean.ReplyBean
import com.judge.data.repository.HomeRepository
import com.judge.data.repository.MineRepository
import com.judge.network.JsonResponse
import io.reactivex.schedulers.Schedulers

data class NewsDetailState(
    val isLoading: Boolean = false,
    val newsDetailResponse: Async<JsonResponse<NewsDetailBean>> = Uninitialized,
    val commentResult: Async<JsonResponse<CommonResultBean>> = Uninitialized,
    val isPageFinished: Boolean = false,
    val newsId: String
) : MvRxState {
    constructor(args: News) : this(newsId = args.tid)
}

class NewsDetailViewModel(
    initialState: NewsDetailState
) : MvRxViewModel<NewsDetailState>(initialState) {
    val map = hashMapOf("version" to "4", "module" to "viewthread")

    fun fetchNewsDetail(tid: String) {
        map["tid"] = tid
        HomeRepository.fetchNewsDetail(map)
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                setState { copy(isLoading = true) }
            }
            .doFinally {
                setState { copy(isLoading = false) }
            }
            .execute {
                copy(newsDetailResponse = it)
            }
    }

    fun setPageFinished(boolean: Boolean) = withState {
        if (it.isPageFinished) return@withState
        setState {
            copy(isPageFinished = boolean)
        }
    }

    fun sendNewsComment(forumId: String, newsId: String, message: String) {
        val map = hashMapOf(
            "fid" to forumId,
            "tid" to newsId,
            "message" to message,
            "formhash" to (MineRepository.userProfile?.formhash ?: ""),
            "replysubmit" to "1"
        )
        HomeRepository.sendNewsComment(map).subscribeOn(Schedulers.io())
            .execute {
                copy(commentResult = it)
            }
    }

    //添加收藏
    fun addToFavorite(newsId: String) {
        val map = hashMapOf(
            "id" to newsId,
            "formhash" to (MineRepository.userProfile?.formhash ?: ""),
            "module" to "favthread",
            "version" to "4"
        )

        HomeRepository.addToFavorite(map).subscribeOn(Schedulers.io())
            .execute {
                copy(commentResult = it)
            }
    }

    fun deleteFavorite(id:String){
        val queryMap =
            hashMapOf("version" to "4", "module" to "myfav_delete", "checkall" to "1")
        val fieldMap =
            hashMapOf(
                "formhash" to (MineRepository.userProfile?.formhash ?: ""),
                "delfavorite" to "true"
            )
        val ids = listOf(id)
        MineRepository.deleteTopics(queryMap, fieldMap, ids)
            .subscribeOn(Schedulers.io())
            .execute {
                copy(commentResult = it)
            }
    }

    //回复发表的评论
    fun commentToPerson(forumId: String, bean: ReplyBean) {
        val map = hashMapOf(
            "fid" to forumId,
            "tid" to bean.tid,
            "message" to bean.message,
            "noticetrimstr" to bean.noticetrimstr,
            "formhash" to (MineRepository.userProfile?.formhash ?: ""),
            "replysubmit" to "1"
        )
        HomeRepository.sendNewsComment(map).subscribeOn(Schedulers.io())
            .execute {
                copy(commentResult = it)
            }
    }
}