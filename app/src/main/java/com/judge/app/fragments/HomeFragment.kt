package com.judge.app.fragments

import androidx.navigation.fragment.findNavController
import com.airbnb.mvrx.*
import com.judge.app.core.BaseFragment
import com.judge.app.core.MvRxEpoxyController
import com.judge.app.core.simpleController
import com.judge.models.DogViewModel
import com.judge.views.dogRow
import com.judge.views.loadingView
import com.judge.R

class HomeFragment : BaseFragment() {
    private val viewModel: DogViewModel by fragmentViewModel()
    override fun epoxyController(): MvRxEpoxyController = simpleController(viewModel) { state ->
        loadingView {
            id("loader")
            loading(state.isLoading)
        }
        state.dogs.forEachIndexed { index, dog ->
            dogRow {
                id(dog.id + index)
                dog(dog)
                textColor(dog.color)
                clickListener { _ ->
                    viewModel.setItemState(index, dog)
                    findNavController().navigate(R.id.action_homeFragment_to_detailFragment, DetailFragment.arg(dog))

                }
            }
        }
    }

    override fun initRefreshLayout() {
        super.initRefreshLayout()
        refreshLayout.apply {
            setEnableAutoLoadMore(true)
            setEnableRefresh(true)
            setEnableLoadMore(true)
            setOnRefreshListener {
                viewModel.refreshDogs()
                it.finishRefresh(1000)
            }
            setOnLoadMoreListener {
                viewModel.fetchDogs()
                it.finishLoadMore(1000)
            }
        }
    }

    override fun onNetWorkChanged(state: Boolean) {
        if (state) initData()
    }
}