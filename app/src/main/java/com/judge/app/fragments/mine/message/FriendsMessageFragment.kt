package com.judge.app.fragments.mine.message

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
import com.judge.data.bean.Friend
import com.judge.data.bean.FriendMessage
import com.judge.data.repository.MineRepository
import com.judge.friendMessageLeftView
import com.judge.friendMessageRightView
import com.judge.utils.CenterTitle
import com.judge.utils.LogUtils
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.friend_tip_view.view.*
import org.jetbrains.anko.sdk27.coroutines.onClick


data class FriendMessageState(
    val isLoading: Boolean = false,
    val friendMessages: List<FriendMessage>? = emptyList(),
    val friend: Friend
) : MvRxState {
    constructor(args: Friend) : this(friend = args)
}

class FriendMessageViewModel(
    initialState: FriendMessageState
) : MvRxViewModel<FriendMessageState>(initialState) {
    private val map = hashMapOf("version" to "4", "module" to "mypm", "plid" to "33", "subop" to "view")

    init {
        fetchFriendMessages()
    }

    private fun fetchFriendMessages() = withState { state ->
        if (state.isLoading) return@withState
        MineRepository.getFriendsMessage(map).subscribeOn(Schedulers.io())
            .doOnSubscribe {
                setState { copy(isLoading = true) }
            }
            .doOnError {
                it.message?.let { it1 -> LogUtils.e(it1) }
            }
            .doFinally { setState { copy(isLoading = false) } }
            .execute {
                copy(friendMessages = it()?.Variables?.list)
            }
    }

    companion object : MvRxViewModelFactory<FriendMessageViewModel, FriendMessageState> {
        override fun create(viewModelContext: ViewModelContext, state: FriendMessageState): FriendMessageViewModel? {
            return FriendMessageViewModel(state)
        }
    }
}


class FriendsMessageFragment : BaseFragment() {
    private val viewModel: FriendMessageViewModel by fragmentViewModel()
    override fun epoxyController(): MvRxEpoxyController = simpleController(viewModel) { state ->
        state.friendMessages?.forEachIndexed { index, message ->
            if (message.msgfromid == MineRepository.userProfile?.space?.uid) {
                friendMessageRightView {
                    id(message.message + index)
                    messageBean(message)
                }
            } else {
                friendMessageLeftView {
                    id(message.message + index)
                    messageBean(message)
                }
            }
        }
    }

    override fun initView() {
        super.initView()
        viewModel.selectSubscribe(FriendMessageState::friend) {
            toolbar.isVisible = true
            toolbar.title = it.username
            CenterTitle.centerTitle(toolbar, true)
        }
        titleViewStub.layoutResource = R.layout.friend_tip_view
        titleViewStub.inflate().apply {
            addFriend.onClick {

            }
        }
    }
}