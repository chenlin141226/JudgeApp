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
import com.judge.data.repository.MineRepository
import com.judge.extensions.copy
import com.judge.friendItemView
import com.judge.searchEditView
import com.judge.utils.LogUtils
import com.judge.views.SimpleTextWatcher
import io.reactivex.schedulers.Schedulers
import java.util.*


data class FriendState(
    val isLoading: Boolean = false,
    val friends: List<Friend> = emptyList()
) : MvRxState

class FriendViewModel(
    initialState: FriendState
) : MvRxViewModel<FriendState>(initialState) {
    private val map = hashMapOf("version" to "4", "module" to "friend")
    private lateinit var friendList: List<Friend>

    init {
        fetchFriends()
    }

    private fun fetchFriends() = withState { state ->
        if (state.isLoading) return@withState
        MineRepository.getFriends(map).subscribeOn(Schedulers.io())
            .doOnSubscribe {
                setState { copy(isLoading = true) }
            }
            .doOnError {
                it.message?.let { it1 -> LogUtils.e(it1) }
            }
            .doFinally { setState { copy(isLoading = false) } }
            .execute {
                friendList = it()?.Variables?.list ?: emptyList()
                copy(friends = it()?.Variables?.list ?: emptyList())
            }
    }

    fun searchFriends(text: String) {
        if (text.isEmpty()) {
            setState { copy(friends = friendList) }
        } else {
            val list = friendList.filter {
                it.username.contains(text)
            }
            setState {
                copy(friends = list)
            }
        }
    }

    companion object : MvRxViewModelFactory<FriendViewModel, FriendState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: FriendState
        ): FriendViewModel? {
            return FriendViewModel(state)
        }
    }
}

class FriendsFragment : BaseFragment() {
    private val friendViewModel: FriendViewModel by fragmentViewModel()
    override fun epoxyController(): MvRxEpoxyController =
        simpleController(friendViewModel) { state ->
            searchEditView {
                id("search friend")
                textWatcher(SimpleTextWatcher {
                    friendViewModel.searchFriends(it)
                })
            }

            state.friends.forEachIndexed { index, friend ->
                friendItemView {
                    id(friend.uid + index)
                    friend(friend)
                    onDeleteClick { _ ->
                    }
                    onItemClick { _ ->
                        navigateTo(R.id.action_friendsFragment_to_friendsMessageFragment, friend)
                    }
                }

            }
        }

    override fun initView() {
        super.initView()
        toolbar.isVisible = true
        sharedViewModel.setVisible(false)
    }
}