package com.judge.app.fragments.judge

import android.annotation.SuppressLint
import android.view.Gravity
import android.widget.TableLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.flyco.tablayout.SlidingTabLayout
import com.judge.R
import com.judge.adapters.ViewPagerAdapter
import com.judge.app.core.BaseFragment
import com.judge.app.core.MvRxEpoxyController
import com.judge.app.core.simpleController
import kotlinx.android.synthetic.main.topic_view.view.*

class JudgeFragment : BaseFragment() {


    override fun epoxyController(): MvRxEpoxyController = simpleController() {

    }

    @SuppressLint("WrongConstant")
    override fun initView() {
        val titles = arrayOf(
            resources.getString(R.string.attention),
            resources.getString(R.string.plate),
            resources.getString(R.string.recommend),
            resources.getString(R.string.information)
        )

        val fragments = ArrayList<Fragment>().also {
            it.add(AttentionFragment())
            it.add(EditionFragment())
            it.add(RecommendFragment())
            it.add(InformationFragment())
        }

        //使用Viewstub添加布局
        titleViewStub.layoutResource = R.layout.judge_view
        titleViewStub.inflate().apply {
            viewPager.adapter = ViewPagerAdapter(childFragmentManager, fragments, titles)
            viewPager.offscreenPageLimit = 4
            tabLayout.setViewPager(viewPager)
        }
    }

    override fun initData() {

    }
}