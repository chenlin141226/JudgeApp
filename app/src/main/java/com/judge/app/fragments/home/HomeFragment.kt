package com.judge.app.fragments.home

import com.airbnb.mvrx.*
import com.judge.app.core.BaseFragment
import com.judge.app.core.MvRxEpoxyController
import com.judge.app.core.simpleController
import com.judge.app.core.MvRxViewModel
import com.judge.data.bean.BannerBean

data class HomeState(
    val banner: BannerBean? = null,
    val isLoading: Boolean = false
) : MvRxState

class HomeViewModel(
    state: HomeState
) : MvRxViewModel<HomeState>(state)

class HomeFragment : BaseFragment() {
    private val viewModel: HomeViewModel by fragmentViewModel()
    override fun epoxyController(): MvRxEpoxyController = simpleController(viewModel) { state ->

    }

    override fun initView() {
        refreshLayout.apply {
            setEnableAutoLoadMore(true)
            setEnableRefresh(true)
            setEnableLoadMore(true)
            setOnRefreshListener {
                it.finishRefresh(1000)
            }
            setOnLoadMoreListener {
                it.finishLoadMore(1000)
            }
        }
    }

    override fun initData() {
        sharedViewModel.setVisible(true)
    }

}