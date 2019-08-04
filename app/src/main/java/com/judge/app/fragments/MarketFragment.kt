package com.judge.app.fragments


import androidx.core.view.isVisible
import com.airbnb.mvrx.fragmentViewModel
import com.judge.R
import com.judge.app.activities.HomeActivity
import com.judge.app.core.BaseFragment
import com.judge.app.core.MvRxEpoxyController
import com.judge.app.core.simpleController
import com.judge.models.VideoViewModel
import com.judge.views.loadingView
import org.jetbrains.anko.sdk27.coroutines.onClick

/**
 * 积分商城Fragment
 */
class MarketFragment : BaseFragment() {
    private val videoViewModel: VideoViewModel by fragmentViewModel()

    override fun epoxyController(): MvRxEpoxyController = simpleController(videoViewModel) { state ->
        loadingView {
            id("loader")
            loading(state.isLoading)
        }

    }

    override fun initData() {
        toolbar.isVisible = true
        setHasOptionsMenu(true)
        (activity as HomeActivity).setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        rightButton.apply {
            isVisible = true
            text = resources.getString(R.string.me)
            onClick {

           }
        }

        titleViewStub.layoutResource = R.layout.market_tablelayout_title
    }

}