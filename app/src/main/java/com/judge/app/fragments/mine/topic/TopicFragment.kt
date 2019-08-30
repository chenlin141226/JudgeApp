package com.judge.app.fragments.mine.topic

import android.view.View
import androidx.fragment.app.Fragment
import com.airbnb.mvrx.*
import com.judge.R
import com.judge.adapters.ViewPagerAdapter
import com.judge.app.core.BaseFragment
import com.judge.app.core.MvRxEpoxyController
import com.judge.app.core.MvRxViewModel
import com.judge.app.core.simpleController
import com.judge.data.bean.CommonResultBean
import com.judge.data.bean.Topic
import com.judge.data.repository.MineRepository
import com.judge.extensions.delete
import com.judge.network.JsonResponse
import com.judge.utils.LogUtils
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.topic_view.view.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

data class TopicState(
    val topicItems: List<Topic> = emptyList(),
    val isLoading: Boolean = false,
    val deleteResult: Async<JsonResponse<CommonResultBean>> = Uninitialized,
    val isSwipeEnable: Boolean = false
) : MvRxState

class TopicViewModel(
    initialState: TopicState
) : MvRxViewModel<TopicState>(initialState) {
    private lateinit var topicMap: HashMap<String, String>
    private lateinit var queryMap: HashMap<String, String>
    private lateinit var fieldMap: HashMap<String, String>
    private var deleteIndex = -1
    fun fetchPublishedTopics() {
        topicMap = hashMapOf("version" to "4", "module" to "mythread", "page" to "1")
        fetchTopics()
    }

    fun fetchHistoryTopics() {
        topicMap = hashMapOf("version" to "4", "module" to "mythread", "page" to "1")
        fetchTopics()
    }

    fun fetchFavoriteTopics() {
        topicMap =
            hashMapOf("version" to "4", "module" to "myfavthread", "start" to "0", "limit" to "20")
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
        val ids = LinkedList<String>()
        deleteIndex = index
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
            "history" -> {

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

    fun deleteTopic() {
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

    companion object : MvRxViewModelFactory<TopicViewModel, TopicState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: TopicState
        ): TopicViewModel? {
            return TopicViewModel(state)
        }
    }
}

class TopicFragment : BaseFragment() {

    override fun epoxyController(): MvRxEpoxyController = simpleController {
    }

    override fun initView() {
        super.initView()
        toolbar.visibility = View.VISIBLE
        val titles = arrayOf(getString(R.string.published_topic), getString(R.string.replied_topic))
        val fragments = ArrayList<Fragment>().also {
            it.add(PublishedTopicFragment())
            it.add(RepliedTopicFragment())
        }
        titleViewStub.layoutResource = R.layout.topic_view
        titleViewStub.inflate().apply {
            viewPager.adapter = ViewPagerAdapter(childFragmentManager, fragments, titles)
            tabLayout.setViewPager(viewPager)
        }
    }
}