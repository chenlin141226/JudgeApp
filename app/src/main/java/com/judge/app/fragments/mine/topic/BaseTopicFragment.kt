package com.judge.app.fragments.mine.topic

import com.airbnb.mvrx.fragmentViewModel
import com.judge.app.core.BaseFragment
import com.judge.app.core.MvRxEpoxyController
import com.judge.app.core.simpleController
import com.judge.historyTopicItem
import com.judge.models.TopicState
import com.judge.models.TopicViewModel
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
                isSwipeEnable(state.isSwipeEnable)
                onItemClick { _ ->
                    //toast("You clicked item!")
                }
                onDeleteClick { _ ->
                    deleteTopics(index)
                    toast("Item index  is $index!")
                }
            }
        }
        state.historyTopics.forEachWithIndex { index, topic ->
            historyTopicItem {
                id(topic.topicTitle + index)
                topic(topic)
                isSwipeEnable(state.isSwipeEnable)
                onItemClick { _ ->
                    //toast("You clicked item!")
                }
                onDeleteClick { _ ->
                    deleteTopics(index)
                    toast("Item index  is $index!")
                }
            }
        }
    }

    override fun initView() {
        super.initView()
        sharedViewModel.setVisible(false)
        viewModel.selectSubscribe(TopicState::isLoading) {
            if (it) {
                loadingDialog.show()
            } else {
                loadingDialog.dismiss()
            }
        }
    }

    open fun deleteTopics(index: Int) {}
}