package com.judge.app.fragments.home

import androidx.core.view.isVisible
import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.fragmentViewModel
import com.github.lzyzsd.jsbridge.BridgeWebView
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
import com.judge.utils.LogUtils
import com.judge.views.ShareBottomPopupView
import com.lxj.xpopup.interfaces.OnSelectListener
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.news_detail_view.*
import kotlinx.android.synthetic.main.news_detail_view.view.*
import org.jetbrains.anko.appcompat.v7.coroutines.onMenuItemClick
import org.jetbrains.anko.support.v4.toast

data class NewsDetailState(
    val isLoading: Boolean = false,
    val newsDetailResponse: Async<JsonResponse<NewsDetailBean>> = Uninitialized,
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

            }.execute {
                copy(newsDetailResponse = it)
            }
    }
}

class NewsDetailFragment : BaseFragment() {
    private val newsDetailUrl = "http://10.5.45.221:8080/"
    private val viewModel: NewsDetailViewModel by fragmentViewModel()
    private lateinit var detailWebView: BridgeWebView
    override fun epoxyController(): MvRxEpoxyController = simpleController {
    }

    override fun initView() {
        super.initView()
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
        titleViewStub.inflate().apply {
            detailWebView = webView

            //从H5 Js 获取数据
            webView.registerHandler("") { data, function ->
                function.onCallBack(data)
            }

            //向H5 Js 发送数据
            /*webView.callHandler("", Gson().toJson()) { responseData ->
                LogUtils.e(responseData)
            }*/

        }
    }

    override fun initData() {
        super.initData()
        viewModel.selectSubscribe(NewsDetailState::newsId) {
            viewModel.fetchNewsDetail(it)
        }
        viewModel.selectSubscribe(NewsDetailState::isLoading) {
            if (it) {
                loadingDialog.show()
            } else {
                loadingDialog.dismiss()
            }
        }
        viewModel.asyncSubscribe(NewsDetailState::newsDetailResponse, onSuccess = {
            detailWebView.callHandler("getInfo", Gson().toJson(it)) { result ->
                webView.loadUrl(newsDetailUrl)
                LogUtils.e(result)
            }
        })
    }

}