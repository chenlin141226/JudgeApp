package com.judge.app.fragments.mine.topic

import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.airbnb.mvrx.fragmentViewModel
import com.judge.app.core.BaseFragment
import com.judge.app.core.MvRxEpoxyController
import com.judge.app.core.MvRxViewModel
import com.judge.app.core.simpleController
import com.judge.data.bean.Topic
import com.judge.data.repository.MineRepository
import com.judge.extensions.delete
import com.judge.topicItem
import com.judge.utils.LogUtils
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.collections.forEachWithIndex
import org.jetbrains.anko.support.v4.toast


data class RepliedTopicState(
    val topicItems: List<Topic> = emptyList(),
    val isLoading: Boolean = false
) : MvRxState

class RepliedTopicViewModel(
    initialState: RepliedTopicState
) : MvRxViewModel<RepliedTopicState>(initialState) {
    private val map = hashMapOf("version" to "4", "module" to "mythread", "start" to "0", "limit" to "20")

    init {
        fetchTopics()
    }

    fun fetchTopics() {
        MineRepository.getPublishedTopics(map)
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

    fun refreshTopics() {
        MineRepository.getPublishedTopics(map)
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

    companion object : MvRxViewModelFactory<RepliedTopicViewModel, RepliedTopicState> {
        override fun create(viewModelContext: ViewModelContext, state: RepliedTopicState): RepliedTopicViewModel? {
            return RepliedTopicViewModel(state)
        }
    }
}

class RepliedTopicFragment : BaseFragment() {
    private val viewModel: RepliedTopicViewModel by fragmentViewModel()
    override fun epoxyController(): MvRxEpoxyController = simpleController(viewModel) { state ->
        state.topicItems.forEachWithIndex { index, topic ->
            topicItem {
                id(topic.subject + index)
                topic(topic)
                onItemClick { _ ->
                    toast("You clicked item!")
                }
                onDeleteClick { _ ->
                    toast("You clicked delete button!")
                }
            }
        }
    }

}