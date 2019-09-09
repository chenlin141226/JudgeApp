package com.judge.models

import com.airbnb.mvrx.*
import com.judge.app.core.MvRxViewModel
import com.judge.data.bean.BannerBean
import com.judge.data.bean.News
import com.judge.data.repository.HomeRepository
import com.judge.db.bean.HistoryTopicBean
import com.judge.db.dao.HistoryTopicDao
import com.judge.db.database.AppDataBase
import com.judge.network.JsonResponse
import com.judge.utils.LogUtils
import com.vondear.rxtool.RxTool
import io.reactivex.schedulers.Schedulers

data class HomeState(
    val responseBean: Async<JsonResponse<BannerBean>> = Uninitialized,
    val news: List<News>? = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false
    //val number: Long = -1
) : MvRxState

class HomeViewModel(
    state: HomeState,
    private val historyTopicDao: HistoryTopicDao
) : MvRxViewModel<HomeState>(state) {
    private val bannerMap = hashMapOf("bid" to "151", "module" to "get_diy")
    private val newsMap = hashMapOf("start" to "0", "module" to "latestthreads", "limit" to "20")

    init {
        fetchBanners()
        fetchNews()
    }

    private fun fetchBanners() {
        HomeRepository.fetchBanners(bannerMap)
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                setState { copy(isLoading = true) }
            }
            .doOnError {
                it.message?.let { it1 -> LogUtils.e(it1) }
            }
            .doFinally { setState { copy(isLoading = false) } }
            .execute {
                copy(responseBean = it)
            }
    }

    fun fetchNews() {
        HomeRepository.fetchNews(newsMap)
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                setState { copy(isLoading = true, isRefreshing = true) }
            }
            .doOnError {
                it.message?.let { it1 -> LogUtils.e(it1) }
            }
            .doFinally { setState { copy(isLoading = false, isRefreshing = false) } }
            .execute {
                copy(news = news?.plus((it()?.Variables?.data ?: emptyList())))
            }
    }

    fun refreshNews() {
        HomeRepository.fetchNews(newsMap)
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                setState { copy(isRefreshing = true) }
            }
            .doOnError {
                it.message?.let { it1 -> LogUtils.e(it1) }
            }
            .doFinally { setState { copy(isRefreshing = false) } }
            .execute {
                copy(news = it()?.Variables?.data ?: emptyList())
            }
    }

    fun insertHistoryTopicItem(bean: HistoryTopicBean) {
        historyTopicDao.insertHistoryTopic(bean).subscribeOn(Schedulers.io())
            .execute {
                copy(
                    //number = it() ?: -1
                )
            }
    }

    companion object : MvRxViewModelFactory<HomeViewModel, HomeState> {
        override fun create(viewModelContext: ViewModelContext, state: HomeState): HomeViewModel? {
            return HomeViewModel(
                state,
                AppDataBase.getInstance(RxTool.getContext()).historyTopicDao()
            )
        }
    }
}