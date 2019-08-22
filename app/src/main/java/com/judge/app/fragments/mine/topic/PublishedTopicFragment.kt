package com.judge.app.fragments.mine.topic

import com.airbnb.mvrx.*
import com.judge.app.core.BaseFragment
import com.judge.app.core.MvRxEpoxyController
import com.judge.app.core.MvRxViewModel
import com.judge.app.core.simpleController
import com.judge.data.bean.Topic
import com.judge.data.repository.MineRepository
import com.judge.extensions.delete
import com.judge.network.services.MineApIService
import com.judge.topicItem
import com.judge.utils.LogUtils
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.collections.forEachWithIndex
import org.jetbrains.anko.support.v4.toast

data class PublicTopicState(
    val topicItems: List<Topic> = emptyList(),
    val isLoading: Boolean = false
) : MvRxState

class PublicTopicViewModel(
    initialState: PublicTopicState
) : MvRxViewModel<PublicTopicState>(initialState) {
    private val map = hashMapOf("version" to "4", "module" to "mythread", "page" to "1")

    init {
        fetchTopics()
    }

    private fun fetchTopics() = withState { state ->
        if (state.isLoading) return@withState
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

    fun refreshTopics() = withState { state ->
        if (state.isLoading) return@withState
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

    companion object : MvRxViewModelFactory<PublicTopicViewModel, PublicTopicState> {
        override fun create(viewModelContext: ViewModelContext, state: PublicTopicState): PublicTopicViewModel? {
            return PublicTopicViewModel(state)
        }
    }
}

class PublishedTopicFragment : BaseFragment() {
    private val viewModel: PublicTopicViewModel by fragmentViewModel()
    override fun epoxyController(): MvRxEpoxyController = simpleController(viewModel) { state ->
        state.topicItems.forEachWithIndex { index, topic ->
            topicItem {
                id(topic.subject + index)
                topic(topic)
                onItemClick { _ ->
                    toast("You clicked item!")
                }
                onDeleteClick { _ ->
                    viewModel.deleteTopic(index)
                    toast("Item index  is $index!")
                }
            }

        }

    }

}