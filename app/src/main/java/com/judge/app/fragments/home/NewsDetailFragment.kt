package com.judge.app.fragments.home

import androidx.core.view.isVisible
import com.judge.R
import com.judge.app.core.BaseFragment
import com.judge.app.core.MvRxEpoxyController
import com.judge.app.core.simpleController
import com.judge.views.ShareBottomPopupView
import com.lxj.xpopup.interfaces.OnSelectListener
import kotlinx.android.synthetic.main.news_detail_view.view.*
import org.jetbrains.anko.appcompat.v7.coroutines.onMenuItemClick
import org.jetbrains.anko.support.v4.toast

class NewsDetailFragment : BaseFragment() {
    override fun epoxyController(): MvRxEpoxyController = simpleController {
    }

    override fun initView() {
        super.initView()
        setHasOptionsMenu(true)
        toolbar.apply {
            inflateMenu(R.menu.menu_main)
            isVisible = true
            onMenuItemClick { item ->
                when (item?.itemId) {
                    R.id.action_share -> {
                        toast("share")
                        ShareBottomPopupView(context)
                            .setOnSelectListener(OnSelectListener { position, _ ->

                            })
                            .showPopup()
                    }
                    else -> {
                    }
                }
            }
        }
        titleViewStub.layoutResource = R.layout.news_detail_view
        titleViewStub.inflate().apply {
            //注册handler向H5 发送数据
            webView.registerHandler("") { data, function ->

            }

            //通过handlerName 从H5获取数据
            webView.callHandler("", "data") { responseData ->

            }
        }
    }

    private fun reply(content: String) {

    }

    private fun collect() {

    }

    private fun replyForPerson() {

    }
}