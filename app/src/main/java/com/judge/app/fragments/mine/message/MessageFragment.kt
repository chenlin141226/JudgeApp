package com.judge.app.fragments.mine.message

import android.view.View
import androidx.core.view.isVisible
import com.judge.R
import com.judge.app.core.BaseFragment
import com.judge.app.core.MvRxEpoxyController
import com.judge.app.core.simpleController
import com.judge.messageItem
import org.jetbrains.anko.sdk27.coroutines.onClick

class MessageFragment : BaseFragment() {
    override fun epoxyController(): MvRxEpoxyController = simpleController {
        messageItem {
            id("personal message")
            title("私人消息")
            messageType(0)
            onClick { _ ->
                navigateTo(R.id.action_messageFragment_to_personalMessageFragment, null)
            }
        }
        messageItem {
            id("public message")
            title("公告消息")
            messageType(1)
            onClick { _ ->
                navigateTo(R.id.action_messageFragment_to_publicMessageFragment, null)
            }
        }
    }

    override fun initView() {
        super.initView()
        toolbar.visibility = View.VISIBLE
        rightButton.apply {
            isVisible = true
            text = getString(R.string.friends)
            onClick {
                navigateTo(R.id.action_messageFragment_to_friendsFragment)
            }
        }
    }
}