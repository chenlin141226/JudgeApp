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
import com.judge.data.bean.PublicMessage
import com.judge.data.repository.MineRepository
import com.judge.publicMessageItem
import com.judge.utils.LogUtils
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.collections.forEachWithIndex
import org.jetbrains.anko.sdk27.coroutines.onClick


data class PublicMessageState(
    val messageItems: List<PublicMessage>? = emptyList(),
    val isLoading: Boolean = false
) : MvRxState

class PublicMessageViewModel(
    initialState: PublicMessageState
) : MvRxViewModel<PublicMessageState>(initialState) {
    private val map = hashMapOf("version" to "4", "module" to "publicpm", "page" to "1")

    init {
        getMessages()
    }

    private fun getMessages() = withState { state ->
        if (state.isLoading) return@withState
        MineRepository.getPublicMessages(map).subscribeOn(Schedulers.io())
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
        state.messageItems?.forEachWithIndex { index, messageBean ->
            publicMessageItem {
                id(messageBean.id + index)
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