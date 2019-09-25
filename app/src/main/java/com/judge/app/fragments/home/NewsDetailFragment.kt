package com.judge.app.fragments.home

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.view.isVisible
import com.airbnb.mvrx.fragmentViewModel
import com.google.gson.Gson
import com.judge.R
import com.judge.app.core.BaseFragment
import com.judge.app.core.MvRxEpoxyController
import com.judge.app.core.simpleController
import com.judge.data.bean.ReplyBean
import com.judge.models.NewsDetailState
import com.judge.models.NewsDetailViewModel
import com.judge.views.ShareBottomPopupView
import com.lxj.xpopup.interfaces.OnSelectListener
import kotlinx.android.synthetic.main.news_detail_view.view.*
import org.jetbrains.anko.appcompat.v7.coroutines.onMenuItemClick
import org.jetbrains.anko.support.v4.toast


class NewsDetailFragment : BaseFragment() {
    private val newsDetailUrl = "http://10.5.45.221:8080/"
    private val viewModel: NewsDetailViewModel by fragmentViewModel()
    private lateinit var detailWebView: WebView
    private lateinit var newsId: String
    private lateinit var forumId: String
    private lateinit var favId:String
    private var isFavorite: Boolean = false
    override fun epoxyController(): MvRxEpoxyController = simpleController {
    }

    @SuppressLint("SetJavaScriptEnabled", "JavascriptInterface")
    override fun initView() {
        super.initView()
        sharedViewModel.setVisible(false)
        setHasOptionsMenu(true)
        refreshLayout.isVisible = false
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
        titleViewStub.run {
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        }
        titleViewStub.inflate().apply {
            detailWebView = webView
            detailWebView.settings.javaScriptEnabled = true
            detailWebView.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    viewModel.setPageFinished(true)
                }
            }
            detailWebView.addJavascriptInterface(this@NewsDetailFragment, "android")
            detailWebView.loadUrl(newsDetailUrl)
        }
    }

    override fun initData() {
        super.initData()
        viewModel.selectSubscribe(NewsDetailState::newsId) {
            newsId = it
        }
        viewModel.selectSubscribe(NewsDetailState::isLoading) {
            if (it) {
                loadingDialog.show()
            } else {
                loadingDialog.dismiss()
            }
        }
        viewModel.selectSubscribe(NewsDetailState::isPageFinished) {
            if (it) {
                viewModel.fetchNewsDetail(newsId)
            }
        }
        viewModel.asyncSubscribe(NewsDetailState::newsDetailResponse, onSuccess = {
            forumId = it.Variables.fid
            isFavorite = it.Variables.isfav == "1"
            favId = it.Variables.favid
            detailWebView.loadUrl("javascript:getInfo(" + Gson().toJson(it) + ")")
        })
        viewModel.asyncSubscribe(NewsDetailState::commentResult, onSuccess = {
            viewModel.fetchNewsDetail(newsId)
        })
    }

    @JavascriptInterface
    fun replay(message: String) {
        viewModel.sendNewsComment(forumId, newsId, message)
    }

    //H5收藏按钮 回调
    @JavascriptInterface
    fun collect() {
        if (isFavorite) {
            viewModel.deleteFavorite(favId)
        } else {
            viewModel.addToFavorite(newsId)
        }
    }

    @JavascriptInterface
    fun replayForPerson(replayData: String) {
        val bean = Gson().fromJson(replayData, ReplyBean::class.java)
        viewModel.commentToPerson(forumId, bean)
    }
}