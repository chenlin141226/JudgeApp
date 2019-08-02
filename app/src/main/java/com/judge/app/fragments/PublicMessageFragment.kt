package com.judge.app.fragments

import android.view.View
import com.judge.app.core.BaseFragment
import com.judge.app.core.MvRxEpoxyController
import com.judge.app.core.simpleController
import com.judge.messageItem

class PublicMessageFragment :BaseFragment(){
    override fun epoxyController(): MvRxEpoxyController = simpleController {
        messageItem {
            id("personal message")
            title("私人消息")
            messageType(0)
            onClick { _ ->
            }
        }
        messageItem {
            id("public message")
            title("公告消息")
            messageType(1)
            onClick { _ ->
            }
        }
    }

    override fun initView() {
        super.initView()
        toolbar.visibility = View.VISIBLE
    }
}