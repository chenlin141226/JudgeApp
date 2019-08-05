package com.judge.app.fragments.market


import android.graphics.Color
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.google.android.material.tabs.TabLayout
import com.judge.R
import com.judge.adapters.MarketPagerAdapter
import com.judge.app.core.BaseFragment
import com.judge.app.core.MvRxEpoxyController
import com.judge.app.core.simpleController
import com.vondear.rxtool.RxTool
import kotlinx.android.synthetic.main.table_layout.view.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.textColor

/**
 * 积分商城Fragment
 */
class MarketFragment : BaseFragment() {

    override fun epoxyController(): MvRxEpoxyController = simpleController{}


    override fun setToolBar() {
        super.setToolBar()
        toolbar.isVisible = true
        toolbar.setBackgroundColor(Color.WHITE)
        //设置toolbar右侧
        rightButton.apply {
            visibility = View.VISIBLE
            text = resources.getString(R.string.me)
            textColor = ContextCompat.getColor(context,R.color.color_gray)
            onClick {
                navigateTo(R.id.action_whistleFragment_to_whistleRulesFragment, null)
            }
        }
    }

    override fun initView() {
        val titles = arrayOf(
            resources.getString(R.string.virtualCard),
            resources.getString(R.string.videoMembers),
            resources.getString(R.string.technology),
            resources.getString(R.string.gameExtensions)
        )

        val myAdapter = MarketPagerAdapter(childFragmentManager)
        myAdapter.setTitle(titles)
        //使用Viewstub添加布局
        titleViewStub.layoutResource = R.layout.table_layout

        var inflate = titleViewStub.inflate().apply {

            viewPager?.let {
                it.adapter = myAdapter
                it.currentItem = 1
                it.offscreenPageLimit = 3
            }
            tabLayout.setupWithViewPager(viewPager)
            tabLayout.tabMode = TabLayout.MODE_FIXED
        }




    }

    override fun initData() {

    }

}