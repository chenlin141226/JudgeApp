package com.judge.app.fragments.mine

import android.view.View
import androidx.core.view.isVisible
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.airbnb.mvrx.fragmentViewModel
import com.judge.R
import com.judge.app.core.BaseFragment
import com.judge.app.core.MvRxEpoxyController
import com.judge.app.core.MvRxViewModel
import com.judge.app.core.simpleController
import com.judge.data.TopicBean
import com.judge.topicItem
import org.jetbrains.anko.collections.forEachWithIndex
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.toast
import java.util.*


data class HistoryState(
    val topicItems: List<TopicBean> = emptyList()
) : MvRxState

class HistoryViewModel(
    initialState: HistoryState
) : MvRxViewModel<HistoryState>(initialState) {
    private val list = LinkedList<TopicBean>()

    init {
        fetchTopics()
    }

    fun fetchTopics() {
        for (i in 1..20) {
            val topic = TopicBean(
                title = "天下武功，唯快不破，欲练此功，必先自宫",
                topicUserName = "花无缺",
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

    companion object : MvRxViewModelFactory<HistoryViewModel, HistoryState> {
        override fun create(viewModelContext: ViewModelContext, state: HistoryState): HistoryViewModel? {
            return HistoryViewModel(state)
        }
    }
}

class HistoryFragment : BaseFragment() {
    private val viewModel: HistoryViewModel by fragmentViewModel()
    override fun epoxyController(): MvRxEpoxyController = simpleController(viewModel) { state ->
        state.topicItems.forEachWithIndex { index, topicBean ->
            topicItem {
                id(topicBean.title + index)
                topic(topicBean)
                onItemClick { _ ->
                    toast("You clicked item!")
                }
                onDeleteClick { _ ->
                    toast("You clicked delete button!")
                }
            }
        }
    }

    override fun initView() {
        super.initView()
        toolbar.isVisible = true
        rightButton.apply {
            visibility = View.VISIBLE
            text = getString(R.string.clear_all)
            onClick {

            }
        }
    }
}