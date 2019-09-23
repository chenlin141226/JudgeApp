package com.judge.app.fragments.home

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.view.isVisible
import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.fragmentViewModel
import com.google.gson.Gson
import com.judge.R
import com.judge.app.core.BaseFragment
import com.judge.app.core.MvRxEpoxyController
import com.judge.app.core.MvRxViewModel
import com.judge.app.core.simpleController
import com.judge.data.bean.News
import com.judge.data.bean.NewsDetailBean
import com.judge.data.repository.HomeRepository
import com.judge.network.JsonResponse
import com.judge.views.ShareBottomPopupView
import com.lxj.xpopup.interfaces.OnSelectListener
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.news_detail_view.view.*
import org.jetbrains.anko.appcompat.v7.coroutines.onMenuItemClick
import org.jetbrains.anko.support.v4.toast

data class NewsDetailState(
    val isLoading: Boolean = false,
    val newsDetailResponse: Async<JsonResponse<NewsDetailBean>> = Uninitialized,
    val isPageFinished: Boolean = false,
    val newsId: String
) : MvRxState {
    constructor(args: News) : this(newsId = args.tid)
}

class NewsDetailViewModel(
    initialState: NewsDetailState
) : MvRxViewModel<NewsDetailState>(initialState) {
    val map = hashMapOf("version" to "4", "module" to "viewthread")

    fun fetchNewsDetail(tid: String) {
        map["tid"] = tid
        HomeRepository.fetchNewsDetail(map)
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                setState { copy(isLoading = true) }
            }
            .doFinally {
                setState { copy(isLoading = false) }
            }
            .execute {
                copy(newsDetailResponse = it)
            }
    }

    fun setPageFinished(boolean: Boolean) = withState {
        if (it.isPageFinished) return@withState
        setState {
            copy(isPageFinished = boolean)
        }
    }
}

class NewsDetailFragment : BaseFragment() {
    private val newsDetailUrl = "http://10.5.45.221:8080/"
    private val viewModel: NewsDetailViewModel by fragmentViewModel()
    private lateinit var detailWebView: WebView
    private lateinit var newsId: String
    override fun epoxyController(): MvRxEpoxyController = simpleController {
    }

    @SuppressLint("SetJavaScriptEnabled")
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
            detailWebView.loadUrl("javascript:getInfo(" + Gson().toJson(it) + ")")
        })
    }

}