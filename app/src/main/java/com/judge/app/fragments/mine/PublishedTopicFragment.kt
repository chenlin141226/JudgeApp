package com.judge.app.fragments.mine

import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.airbnb.mvrx.fragmentViewModel
import com.judge.app.core.BaseFragment
import com.judge.app.core.MvRxEpoxyController
import com.judge.app.core.MvRxViewModel
import com.judge.app.core.simpleController
import com.judge.data.TopicBean
import com.judge.extensions.delete
import com.judge.topicItem
import org.jetbrains.anko.collections.forEachWithIndex
import org.jetbrains.anko.support.v4.toast
import java.util.*

data class PublicTopicState(
    val topicItems: List<TopicBean> = emptyList()
) : MvRxState

class PublicTopicViewModel(
    initialState: PublicTopicState
) : MvRxViewModel<PublicTopicState>(initialState) {
    private val list = LinkedList<TopicBean>()

    init {
        fetchTopics()
    }

    private fun fetchTopics() {
        for (i in 1..20) {
            val topic = TopicBean(
                title = "天下武功，唯快不破，欲练此功，必先自宫",
                topicUserName = "花无缺$i",
                time = "一天前",
                viewedCount = "1314",
                commentCount = "1314"
            )
            list.add(topic)
        }

        setState {
            copy(topicItems = list)
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
        state.topicItems.forEachWithIndex { index, topicBean ->
            topicItem {
                id(topicBean.title + index)
                topic(topicBean)
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