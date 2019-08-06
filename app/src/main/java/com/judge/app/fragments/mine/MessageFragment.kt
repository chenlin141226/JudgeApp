package com.judge.app.fragments.mine

import android.view.View
import com.judge.R
import com.judge.app.core.BaseFragment
import com.judge.app.core.MvRxEpoxyController
import com.judge.app.core.simpleController
import com.judge.messageItem

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
    }
}