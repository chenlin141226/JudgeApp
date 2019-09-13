package com.judge.app.fragments.market

import com.airbnb.mvrx.*
import com.judge.R
import com.judge.app.core.BaseFragment
import com.judge.app.core.MvRxViewModel
import com.judge.app.core.simpleController
import com.judge.data.bean.CategoryItem
import com.judge.data.repository.JudgeRepository
import com.judge.extensions.clear
import com.judge.marketItem
import com.judge.utils.LogUtils
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.collections.forEachWithIndex

/**
 * @author: jaffa
 * @date: 2019/8/4
 */

data class MarketState(
    val isLoading: Boolean = false,
    val markItem: List<CategoryItem> = emptyList()
) : MvRxState

class MarketViewModel(marketState: MarketState) : MvRxViewModel<MarketState>(marketState) {
//    init {
//        fetchMarkInfo(1)
//    }

    fun fetchMarkInfo(index: Int) = withState { _ ->
        val map = hashMapOf("page" to "1", "fenlei_7ree" to "${index + 1}")
        JudgeRepository.getMarket(map).subscribeOn(Schedulers.io())
            .doOnSubscribe { setState { copy(isLoading = true) } }
            .doOnError {
                it.message?.let { it1 -> LogUtils.e(it1) }
            }
            .doFinally { setState { copy(isLoading = false) } }
            .execute {
                copy(markItem = it()?.Variables?.data?.item ?: emptyList())
            }
    }

    fun loadMoreMarkInfo(page: Int,index: Int) = withState { _ ->

        val map = hashMapOf("page" to "$page", "fenlei_7ree" to "$index")
        JudgeRepository.getMarket(map).subscribeOn(Schedulers.io())
            .doOnSubscribe { setState { copy(isLoading = true) } }
            .doOnError { it.message.let { it1 -> LogUtils.e(it1!!) } }
            .doFinally { setState { copy(isLoading = false) } }
            .execute {
                copy(
                    markItem = markItem.plus(it()?.Variables?.data?.item ?: emptyList())
                )
            }
    }

    fun clearMarkInfo() = withState { state ->
        state.markItem.clear()
        setState { copy(markItem = markItem) }
    }



    companion object : MvRxViewModelFactory<MarketViewModel, MarketState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: MarketState
        ): MarketViewModel? {
            return MarketViewModel(state)
        }
    }
}


class AllProductFragment(index: Int) : BaseFragment() {

    var index = index
    var page = 1
    private val viewModel: MarketViewModel by fragmentViewModel()

    override fun epoxyController() = simpleController(viewModel) { state ->

        state.markItem.forEachWithIndex { index, item ->

            marketItem {
                id(item.id_7ree + index)
                mark(item)
                onParentClick { _ ->
                   navigateTo(R.id.action_marketFragments_to_productDetailsFragment,item)
                }
            }
        }
    }

    override fun initData() {
        viewModel.fetchMarkInfo(index)
        withState(viewModel) { _ ->
            refreshLayout.apply {
                setEnableAutoLoadMore(true)
                setEnableRefresh(true)
                setEnableLoadMore(true)
                setOnRefreshListener {
                    viewModel.fetchMarkInfo(index)
                    it.finishRefresh(1000)
                }
                setOnLoadMoreListener {
                    if (page <= 3) {
                        page++
                        viewModel.loadMoreMarkInfo(page, index)
                    }
                    it.finishLoadMore(1000)
                }
            }
        }
    }


    override fun onDestroyView() {
            super.onDestroy()
            page = 1
            viewModel.clearMarkInfo()
        super.onDestroyView()
    }
}