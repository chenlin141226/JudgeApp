package com.judge.app.fragments.mine.topic

import android.view.View
import androidx.fragment.app.Fragment
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.judge.R
import com.judge.adapters.ViewPagerAdapter
import com.judge.app.core.BaseFragment
import com.judge.app.core.MvRxEpoxyController
import com.judge.app.core.MvRxViewModel
import com.judge.app.core.simpleController
import com.judge.data.bean.Topic
import com.judge.data.repository.MineRepository
import com.judge.extensions.delete
import com.judge.utils.LogUtils
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.topic_view.view.*
import org.jetbrains.anko.sdk27.coroutines.onClick

data class TopicState(
    val topicItems: List<Topic> = emptyList(),
    val isLoading: Boolean = false
) : MvRxState

class TopicViewModel(
    initialState: TopicState
) : MvRxViewModel<TopicState>(initialState) {
    private lateinit var topicMap: HashMap<String, String>

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
            hashMapOf("version" to "4", "module" to "my", "start" to "0", "limit" to "20")
        fetchTopics()
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
                copy(topicItems = topicItems.plus((it()?.Variables?.data ?: emptyList())))
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
                copy(topicItems = (it()?.Variables?.data ?: emptyList()))
            }
    }

    fun deleteTopic(index: Int) {
        setState {
            copy(topicItems = topicItems.delete(index))
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
        toolbar.visibility = View.VISIBLE
        rightButton.apply {
            visibility = View.VISIBLE
            text = getString(R.string.clear_all)
            onClick {

            }
        }
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