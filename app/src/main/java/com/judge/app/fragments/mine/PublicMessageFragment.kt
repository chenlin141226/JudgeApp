package com.judge.app.fragments.mine

import android.view.View
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.airbnb.mvrx.fragmentViewModel
import com.judge.R
import com.judge.app.core.BaseFragment
import com.judge.app.core.MvRxEpoxyController
import com.judge.app.core.MvRxViewModel
import com.judge.app.core.simpleController
import com.judge.data.PublicMessageBean
import com.judge.publicMessageItem
import org.jetbrains.anko.collections.forEachWithIndex
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.util.*


data class PublicMessageState(
    val messageItems: List<PublicMessageBean> = emptyList()
) : MvRxState

class PublicMessageViewModel(
    initialState: PublicMessageState
) : MvRxViewModel<PublicMessageState>(initialState) {
    private val list = LinkedList<PublicMessageBean>()

    init {
        getMessages()
    }

    private fun getMessages() {
        for (i in 1..20) {
            val item = PublicMessageBean(
                title = "天下武功，唯快不破",
                messageTime = "$i 分钟前"
            )
            list.add(item)
        }
        setState {
            copy(messageItems = list)
        }
    }

    companion object : MvRxViewModelFactory<PublicMessageViewModel, PublicMessageState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: PublicMessageState
        ): PublicMessageViewModel? {
            return PublicMessageViewModel(state)
        }
    }
}

class PublicMessageFragment : BaseFragment() {
    private val viewModel: PublicMessageViewModel by fragmentViewModel()
    override fun epoxyController(): MvRxEpoxyController = simpleController(viewModel) { state ->
        state.messageItems.forEachWithIndex { index, messageBean ->
            publicMessageItem {
                id(messageBean.title + index)
                message(messageBean)
            }
        }
    }

    override fun initView() {
        super.initView()
        toolbar.visibility = View.VISIBLE
        rightButton.apply {
            visibility = View.VISIBLE
            text = getString(R.string.ignore_unread)
            onClick {

            }
        }
    }
}