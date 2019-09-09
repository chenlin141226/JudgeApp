package com.judge.app.fragments.market

import android.graphics.Color
import android.view.View
import com.airbnb.mvrx.*
import com.judge.app.core.BaseFragment
import com.judge.app.core.MvRxViewModel
import com.judge.app.core.simpleController
import com.judge.data.bean.MyProduct
import com.judge.data.repository.JudgeRepository
import com.judge.extensions.clear
import com.judge.myproductItem
import com.judge.utils.LogUtils
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.collections.forEachWithIndex

/**
 * @author: jaffa
 * @date: 2019/8/5
 */
data class MyProductState(
    val isLoading: Boolean = false,
    val product: List<MyProduct> = emptyList(),
    val totalPage: String? = null
) : MvRxState

class MyProductViewModel(initialState: MyProductState) :
    MvRxViewModel<MyProductState>(initialState) {

    init {
        fetchMyProduct(1)
    }

    fun fetchMyProduct(page: Int) {
        JudgeRepository.getMyProduct("$page").subscribeOn(Schedulers.io())
            .doOnSubscribe { setState { copy(isLoading = true) } }
            .doOnError {
                it.message?.let { it1 -> LogUtils.e(it1) }
            }
            .doFinally { setState { copy(isLoading = false) } }
            .execute {
                copy(
                    product = it()?.Variables?.data ?: emptyList(),
                    totalPage = it()?.Variables?.total_per_page
                )
            }
    }


    fun loadMoreMyProduct(page: Int) = withState { state ->

        JudgeRepository.getMyProduct("$page").subscribeOn(Schedulers.io())
            .doOnSubscribe { setState { copy(isLoading = true) } }
            .doOnError { it.message.let { it1 -> LogUtils.e(it1!!) } }
            .doFinally { setState { copy(isLoading = false) } }
            .execute {
                copy(
                    product = product.plus(it()?.Variables?.data ?: emptyList())
                )
            }
    }


    fun clearMyProduct() = withState { state ->
        state.product.clear()
        setState { copy(product = product) }
    }

    companion object : MvRxViewModelFactory<MyProductViewModel, MyProductState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: MyProductState
        ): MyProductViewModel? {
            return MyProductViewModel(state)
        }
    }
}


class MyProductFragment : BaseFragment() {

    val viewModel: MyProductViewModel by fragmentViewModel()
    var page = 1
    var totalPage = 0
    override fun epoxyController() = simpleController(viewModel) { state ->

        state.product.forEachWithIndex { index, item ->
            myproductItem {
                id(item.id_7ree)
                product(item)
                state.totalPage?.toInt()?.let {
                    totalPage = it
                }
            }
        }
    }

    override fun initView() {
        super.initView()
        sharedViewModel.setVisible(false)
    }

    override fun initData() {
        toolbar.visibility = View.VISIBLE
        toolbar.setBackgroundColor(Color.WHITE)
        viewModel.fetchMyProduct(page)

        refreshLayout.apply {
            setEnableAutoLoadMore(true)
            setEnableRefresh(true)
            setEnableLoadMore(true)
            setOnRefreshListener {
                viewModel.fetchMyProduct(page)
                it.finishRefresh(1000)
            }
            setOnLoadMoreListener {
                if (page <= totalPage) {
                    page++
                    viewModel.loadMoreMyProduct(page)
                }
                it.finishLoadMore(1000)
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroy()
        page = 1
        viewModel.clearMyProduct()
        super.onDestroyView()
    }
}