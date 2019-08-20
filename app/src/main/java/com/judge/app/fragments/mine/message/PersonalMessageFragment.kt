package com.judge.app.fragments.mine.message

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
import com.judge.data.bean.PersonalMessage
import com.judge.data.repository.MineRepository
import com.judge.personalMessageItem
import com.judge.utils.LogUtils
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.collections.forEachWithIndex
import org.jetbrains.anko.sdk27.coroutines.onClick

data class PersonalMessageState(
    val messageItems: List<PersonalMessage>? = emptyList(),
    val isLoading: Boolean = false
) : MvRxState

class PersonalMessageViewModel(
    initialState: PersonalMessageState
) : MvRxViewModel<PersonalMessageState>(initialState) {
    private val map = hashMapOf("version" to "4", "module" to "mypm")

    init {
        getMessages()
    }

    private fun getMessages() = withState { state ->
        if (state.isLoading) return@withState
        MineRepository.getPersonalMessages(map).subscribeOn(Schedulers.io())
            .doOnSubscribe {
                setState { copy(isLoading = true) }
            }
            .doOnError {
                it.message?.let { it1 -> LogUtils.e(it1) }
            }
            .doFinally { setState { copy(isLoading = false) } }
            .execute {
                copy(messageItems = it()?.Variables?.list)
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
        state.messageItems?.forEachWithIndex { index, item ->
            personalMessageItem {
                id(item.tousername + index)
                message(item)
                onItemClick { _ ->
                    navigateTo(R.id.action_personalMessageFragment_to_friendsMessageFragment, item)
                }
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