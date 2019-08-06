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
import com.judge.data.PersonalMessageBean
import com.judge.personalMessageItem
import org.jetbrains.anko.collections.forEachWithIndex
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.util.*

data class PersonalMessageState(
    val messageItems: List<PersonalMessageBean> = emptyList()
) : MvRxState

class PersonalMessageViewModel(
    initialState: PersonalMessageState
) : MvRxViewModel<PersonalMessageState>(initialState) {
    private val list = LinkedList<PersonalMessageBean>()

    init {
        getMessages()
    }

    private fun getMessages() {
        for (i in 1..20) {
            val item = PersonalMessageBean(
                title = "天下武功，唯快不破",
                messageTime = "$i 分钟前"
            )
            list.add(item)
        }
        setState {
            copy(messageItems = list)
        }
    }

    companion object : MvRxViewModelFactory<PersonalMessageViewModel, PersonalMessageState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: PersonalMessageState
        ): PersonalMessageViewModel? {
            return PersonalMessageViewModel(state)
        }
    }
}

class PersonalMessageFragment : BaseFragment() {
    private val viewModel: PersonalMessageViewModel by fragmentViewModel()
    override fun epoxyController(): MvRxEpoxyController = simpleController(viewModel) { state ->
        state.messageItems.forEachWithIndex { index, item ->
            personalMessageItem {
                id(item.title + index)
                message(item)
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