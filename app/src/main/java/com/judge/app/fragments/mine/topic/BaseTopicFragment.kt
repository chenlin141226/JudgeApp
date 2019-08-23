package com.judge.app.fragments.mine.topic

import com.airbnb.mvrx.fragmentViewModel
import com.judge.app.core.BaseFragment
import com.judge.app.core.MvRxEpoxyController
import com.judge.app.core.simpleController
import com.judge.topicItem
import org.jetbrains.anko.collections.forEachWithIndex
import org.jetbrains.anko.support.v4.toast

abstract class BaseTopicFragment : BaseFragment() {
    protected val viewModel: TopicViewModel by fragmentViewModel()
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