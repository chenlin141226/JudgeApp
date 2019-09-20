package com.judge.app.fragments.home

import android.content.Context
import android.widget.ImageView
import com.airbnb.mvrx.*
import com.bumptech.glide.Glide
import com.judge.R
import com.judge.app.core.BaseFragment
import com.judge.app.core.MvRxEpoxyController
import com.judge.app.core.simpleController
import com.judge.db.bean.HistoryTopicBean
import com.judge.models.HomeState
import com.judge.models.HomeViewModel
import com.judge.network.ServiceCreator
import com.judge.newsItemView
import com.vondear.rxtool.RxTimeTool
import com.youth.banner.Banner
import com.youth.banner.loader.ImageLoader
import org.jetbrains.anko.collections.forEachWithIndex
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment : BaseFragment() {
    private val viewModel: HomeViewModel by fragmentViewModel()
    private lateinit var bannerView: Banner
    override fun epoxyController(): MvRxEpoxyController = simpleController(viewModel) { state ->
        state.news?.forEachWithIndex { index, newsItem ->
            newsItemView {
                id(newsItem.author + index)
                item(newsItem)
                onClick { _ ->
                    viewModel.insertHistoryTopicItem(
                        HistoryTopicBean(
                            topicTitle = newsItem.subject,
                            topicAuthor = newsItem.author,
                            surfedTime = RxTimeTool.date2String(Date()),
                            id = newsItem.tid
                        )
                    )
                    navigateTo(R.id.action_homeFragment_to_newsDetailFragment,newsItem)
                }
            }
        }
    }

    override fun initView() {
        refreshLayout.apply {
            setEnableAutoLoadMore(true)
            setEnableRefresh(true)
            setEnableLoadMore(true)
            setOnRefreshListener {
                viewModel.refreshNews()
            }
            setOnLoadMoreListener {
                viewModel.fetchNews()
            }
        }
        titleViewStub.layoutResource = R.layout.home_header_view
        titleViewStub.inflate().apply {
            bannerView = this.findViewById(R.id.banner)
        }
    }

    override fun initData() {
        sharedViewModel.setVisible(true)
        viewModel.asyncSubscribe(HomeState::responseBean, onSuccess = {
            val images = ArrayList<String>()
            it.Variables.data.forEach { item ->
                images.add(ServiceCreator.BASE_URL + item.pic)
            }
            setBanners(images)
        })
        viewModel.selectSubscribe(HomeState::isRefreshing) {
            if (!it) {
                refreshLayout.finishRefresh()
                refreshLayout.finishLoadMore()
            }
        }
    }

    private fun setBanners(images: ArrayList<String>) {
        bannerView.setImages(images)
            .setImageLoader(object : ImageLoader() {
                override fun displayImage(context: Context, path: Any, imageView: ImageView) {
                    Glide.with(context)
                        .load(path)
                        .into(imageView.apply {
                            scaleType = ImageView.ScaleType.FIT_XY
                        })
                }
            })
            .start()
    }

}