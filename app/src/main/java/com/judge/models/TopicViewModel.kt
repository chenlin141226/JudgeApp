package com.judge.models

import com.airbnb.mvrx.*
import com.judge.app.core.MvRxViewModel
import com.judge.data.bean.CommonResultBean
import com.judge.data.bean.Topic
import com.judge.data.repository.MineRepository
import com.judge.db.bean.HistoryTopicBean
import com.judge.db.dao.HistoryTopicDao
import com.judge.db.database.AppDataBase
import com.judge.extensions.delete
import com.judge.network.JsonResponse
import com.judge.utils.LogUtils
import com.vondear.rxtool.RxTool
import io.reactivex.schedulers.Schedulers
import java.util.*

data class TopicState(
    val topicItems: List<Topic> = emptyList(),
    val historyTopics: List<HistoryTopicBean> = emptyList(),
    val isLoading: Boolean = false,
    val deleteResult: Async<JsonResponse<CommonResultBean>> = Uninitialized,
    val isSwipeEnable: Boolean = false
) : MvRxState

class TopicViewModel(
    initialState: TopicState,
    private val historyTopicDao: HistoryTopicDao
) : MvRxViewModel<TopicState>(initialState) {
    private lateinit var topicMap: HashMap<String, String>
    private lateinit var queryMap: HashMap<String, String>
    private lateinit var fieldMap: HashMap<String, String>
    private var deleteIndex = -1
    private var deleteType = ""
    fun fetchPublishedTopics() {
        topicMap = hashMapOf("version" to "4", "module" to "mythread", "page" to "1")
        fetchTopics()
    }

    fun fetchHistoryTopics() {
        historyTopicDao.getAllTopics().subscribeOn(Schedulers.io())
            .doOnSubscribe {
                setState { copy(isLoading = true) }
            }
            .doOnError {
                it.message?.let { it1 -> LogUtils.e(it1) }
            }
            .doFinally { setState { copy(isLoading = false) } }
            .execute {
                copy(
                    isLoading = false,
                    historyTopics = it() ?: emptyList()
                )
            }
    }

    fun fetchFavoriteTopics() {
        topicMap =
            hashMapOf(
                "version" to "4",
                "module" to "myfavthread",
                "formhash" to (MineRepository.userProfile?.formhash ?: "")
            )
        fetchTopics()
    }

    fun fetchRepliedTopics() {
        topicMap =
            hashMapOf("version" to "4", "module" to "mythread", "start" to "0", "limit" to "20")
        fetchTopics()
    }

    fun setSwipeEnable(isEnable: Boolean) {
        setState {
            copy(isSwipeEnable = isEnable)
        }
    }

    private fun fetchTopics() = withState { state ->
        if (state.isLoading) return@withState
        MineRepository.getPublishedTopics(topicMap)
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                setState { copy(isLoading = true) }
            }
            .doOnError {
                it.message?.let { it1 -> LogUtils.e(it1) }
            }
            .doFinally { setState { copy(isLoading = false) } }
            .execute {
                copy(
                    topicItems = topicItems.plus(
                        (it()?.Variables?.data ?: it()?.Variables?.list ?: emptyList())
                    )
                )
            }
    }

    fun refreshTopics() = withState { state ->
        if (state.isLoading) return@withState
        MineRepository.getPublishedTopics(topicMap)
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                setState { copy(isLoading = true) }
            }
            .doOnError {
                it.message?.let { it1 -> LogUtils.e(it1) }
            }
            .doFinally { setState { copy(isLoading = false) } }
            .execute {
                copy(topicItems = it()?.Variables?.data ?: it()?.Variables?.list ?: emptyList())
            }
    }

    fun deleteTopics(index: Int, type: String) = withState { state ->
        if (state.deleteResult is Loading) return@withState
        deleteIndex = index
        deleteType = type
        if (type == "history") {
            deleteTopic()
        } else {
            val ids = LinkedList<String>()
            if (index == -1) {
                state.topicItems.forEach {
                    ids.add(it.favid)
                }
            } else {
                ids.add(state.topicItems[index].favid)
            }
            when (type) {
                "favorite" -> {
                    queryMap =
                        hashMapOf("version" to "4", "module" to "myfav_delete", "checkall" to "1")
                    fieldMap =
                        hashMapOf(
                            "formhash" to (MineRepository.userProfile?.formhash ?: ""),
                            "delfavorite" to "true"
                        )
                }
                "published" -> {

                }
                "replied" -> {

                }
                else -> {

                }
            }
            MineRepository.deleteTopics(queryMap, fieldMap, ids)
                .subscribeOn(Schedulers.io())
                .execute {
                    copy(deleteResult = it)
                }
        }
    }

    fun deleteTopic() = withState {
        if (deleteType == "history") {
            if (deleteIndex == -1) {
                historyTopicDao.deleteAllHistoryTopics()
                setState {
                    copy(historyTopics = historyTopics.delete(historyTopics))
                }
            } else {
                historyTopicDao.deleteHistoryTopic(it.historyTopics[deleteIndex])
                setState {
                    copy(historyTopics = historyTopics.delete(deleteIndex))
                }
            }
        } else {
            if (deleteIndex == -1) {
                setState {
                    copy(topicItems = topicItems.delete(topicItems))
                }
            } else {
                setState {
                    copy(topicItems = topicItems.delete(deleteIndex))
                }
            }
        }
    }

    companion object : MvRxViewModelFactory<TopicViewModel, TopicState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: TopicState
        ): TopicViewModel? {
            return TopicViewModel(
                state,
                AppDataBase.getInstance(RxTool.getContext()).historyTopicDao()
            )
        }
    }
}
