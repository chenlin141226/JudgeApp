package com.judge.app.fragments.mine.message

import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import com.airbnb.mvrx.*
import com.judge.R
import com.judge.app.core.BaseFragment
import com.judge.app.core.MvRxEpoxyController
import com.judge.app.core.MvRxViewModel
import com.judge.app.core.simpleController
import com.judge.data.bean.Friend
import com.judge.data.bean.FriendMessage
import com.judge.data.bean.MessageSendResultBean
import com.judge.data.bean.PersonalMessage
import com.judge.data.repository.MineRepository
import com.judge.extensions.add
import com.judge.friendMessageLeftView
import com.judge.friendMessageRightView
import com.judge.network.JsonResponse
import com.judge.utils.CenterTitle
import com.judge.utils.LogUtils
import com.vondear.rxtool.RxConstants
import com.vondear.rxtool.RxTimeTool
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.friend_tip_view.view.*
import kotlinx.android.synthetic.main.send_message_view.view.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.runOnUiThread
import org.jetbrains.anko.support.v4.toast


data class FriendMessageState(
    val isLoading: Boolean = false,
    val friendMessages: List<FriendMessage>? = emptyList(),
    val sendResultBean: Async<JsonResponse<MessageSendResultBean>> = Uninitialized,
    val friend: Friend?,
    val personalMessage: PersonalMessage?
) : MvRxState {
    constructor(args: Friend) : this(friend = args, personalMessage = null)
    constructor(args: PersonalMessage) : this(personalMessage = args, friend = null)
}

class FriendMessageViewModel(
    initialState: FriendMessageState
) : MvRxViewModel<FriendMessageState>(initialState) {
    private val map =
        hashMapOf("version" to "4", "module" to "mypm", "plid" to "33", "subop" to "view")
    private lateinit var message: String
    private val profile = MineRepository.userProfile

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

    fun sendMessage(toUid: String, message: String) {
        val formHash = profile?.formhash ?: ""
        this.message = message
        val map =
            hashMapOf(
                "uid" to toUid,
                "message" to message,
                "formhash" to formHash,
                "pmsubmit" to "1"
            )
        MineRepository.sendMessage(map)
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                setState { copy(isLoading = true) }
            }
            .doOnError {
                it.message?.let { it1 -> LogUtils.e(it1) }
            }
            .doFinally { setState { copy(isLoading = false) } }
            .execute {
                copy(sendResultBean = it)
            }
    }

    fun updateMessageList() = withState { state ->
        lateinit var messageBean: FriendMessage
        state.friendMessages?.forEach {
            if (it.msgfromid == profile?.member_uid) {
                messageBean = it
            }
        }
        setState {
            copy(
                friendMessages = friendMessages?.add(
                    messageBean.copy(
                        message = message,
                        vdateline = RxTimeTool.getCurrentDateTime(RxConstants.DATE_FORMAT_DETACH)
                    )
                )
            )
        }
    }

    companion object : MvRxViewModelFactory<FriendMessageViewModel, FriendMessageState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: FriendMessageState
        ): FriendMessageViewModel? {
            return FriendMessageViewModel(state)
        }
    }
}


class FriendsMessageFragment : BaseFragment() {
    private val viewModel: FriendMessageViewModel by fragmentViewModel()
    private var toUid: String = ""
    override fun epoxyController(): MvRxEpoxyController = simpleController(viewModel) {
        it.friendMessages?.forEachIndexed { index, message ->
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
            runOnUiThread {
                if (index == it.friendMessages.size - 1) {
                    recyclerView.scrollToPosition(index)
                }
            }
        }
    }

    override fun initView() {
        super.initView()
        viewModel.selectSubscribe(
            FriendMessageState::friend,
            FriendMessageState::personalMessage
        ) { friend, personalMessage ->
            toolbar.isVisible = true
            toolbar.title = friend?.username ?: personalMessage?.tousername
            toUid = friend?.uid ?: personalMessage?.msgfromid ?: ""
            CenterTitle.centerTitle(toolbar, true)
        }

        viewModel.asyncSubscribe(FriendMessageState::sendResultBean, onSuccess = {
            if (it.Variables.success == "1") {
                viewModel.updateMessageList()
            } else {
                toast(it.Message.messagestr)
            }
        })
        titleViewStub.layoutResource = R.layout.friend_tip_view
        titleViewStub.inflate().apply {
            addFriend.onClick {

            }
        }
        bottomViewStub.layoutResource = R.layout.send_message_view
        bottomViewStub.inflate().apply {
            messageEditText.setOnEditorActionListener { view, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    viewModel.sendMessage(toUid = toUid, message = view.text.toString())
                    view.text = ""
                }
                false
            }
        }
    }
}