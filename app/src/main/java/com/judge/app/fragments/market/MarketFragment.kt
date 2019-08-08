package com.judge.app.fragments.market


import android.graphics.Color
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.judge.R
import com.judge.adapters.ViewPagerAdapter
import com.judge.app.core.BaseFragment
import com.judge.app.core.MvRxEpoxyController
import com.judge.app.core.simpleController
import kotlinx.android.synthetic.main.topic_view.view.*
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
                navigateTo(R.id.action_marketFragment_to_myProductFragment, null)
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

        val fragments = ArrayList<Fragment>().also {
            for (index in 0 until 4) {
                it.add(AllProductFragment(index))
            }
        }

        //使用Viewstub添加布局
        titleViewStub.layoutResource = R.layout.topic_view

         titleViewStub.inflate().apply {
             viewPager.adapter = ViewPagerAdapter(childFragmentManager, fragments, titles)
             viewPager.offscreenPageLimit = 4
             tabLayout.setViewPager(viewPager)
        }

    }

    override fun initData() {

    }

}