package com.judge.app.fragments.home

import android.content.Context
import android.widget.ImageView
import com.airbnb.mvrx.*
import com.bumptech.glide.Glide
import com.judge.R
import com.judge.app.core.BaseFragment
import com.judge.app.core.MvRxEpoxyController
import com.judge.app.core.simpleController
import com.judge.app.core.MvRxViewModel
import com.judge.data.bean.BannerBean
import com.judge.data.bean.News
import com.judge.data.repository.HomeRepository
import com.judge.network.JsonResponse
import com.judge.network.ServiceCreator
import com.judge.newsItemView
import com.judge.utils.LogUtils
import com.youth.banner.Banner
import com.youth.banner.loader.ImageLoader
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.collections.forEachWithIndex

data class HomeState(
    val responseBean: Async<JsonResponse<BannerBean>> = Uninitialized,
    val news: List<News>? = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false
) : MvRxState

class HomeViewModel(
    state: HomeState
) : MvRxViewModel<HomeState>(state) {
    private val bannerMap = hashMapOf("bid" to "151", "module" to "get_diy")
    private val newsMap = hashMapOf("start" to "0", "module" to "latestthreads", "limit" to "20")

    init {
        fetchBanners()
        fetchNews()
    }

    private fun fetchBanners() {
        HomeRepository.fetchBanners(bannerMap)
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                setState { copy(isLoading = true) }
            }
            .doOnError {
                it.message?.let { it1 -> LogUtils.e(it1) }
            }
            .doFinally { setState { copy(isLoading = false) } }
            .execute {
                copy(responseBean = it)
            }
    }

    fun fetchNews() {
        HomeRepository.fetchNews(newsMap)
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                setState { copy(isLoading = true, isRefreshing = true) }
            }
            .doOnError {
                it.message?.let { it1 -> LogUtils.e(it1) }
            }
            .doFinally { setState { copy(isLoading = false, isRefreshing = false) } }
            .execute {
                copy(news = news?.plus((it()?.Variables?.data ?: emptyList())))
            }
    }

    fun refreshNews() {
        HomeRepository.fetchNews(newsMap)
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                setState { copy(isRefreshing = true) }
            }
            .doOnError {
                it.message?.let { it1 -> LogUtils.e(it1) }
            }
            .doFinally { setState { copy(isRefreshing = false) } }
            .execute {
                copy(news = it()?.Variables?.data ?: emptyList())
            }
    }

    companion object : MvRxViewModelFactory<HomeViewModel, HomeState> {
        override fun create(viewModelContext: ViewModelContext, state: HomeState): HomeViewModel? {
            return HomeViewModel(state)
        }
    }
}

class HomeFragment : BaseFragment() {
    private val viewModel: HomeViewModel by fragmentViewModel()
    private lateinit var bannerView: Banner
    override fun epoxyController(): MvRxEpoxyController = simpleController(viewModel) { state ->
        state.news?.forEachWithIndex { index, newsItem ->
            newsItemView {
                id(newsItem.author + index)
                item(newsItem)
                onClick { _ ->
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
                images.add(ServiceCreator.BASE_URL + "/" + item.pic)
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
                    Glide.with(context).load(path).into(imageView)
                }
            })
            .start()
    }

}