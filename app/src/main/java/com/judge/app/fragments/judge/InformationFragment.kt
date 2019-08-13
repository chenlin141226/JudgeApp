package com.judge.app.fragments.judge

import android.graphics.Color
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.airbnb.mvrx.*
import com.judge.R
import com.judge.adapters.ViewPagerAdapter
import com.judge.app.core.BaseFragment
import com.judge.app.core.MvRxViewModel
import com.judge.app.core.simpleController
import com.judge.data.bean.Data
import com.judge.data.repository.JudgeRepository
import com.judge.utils.LogUtils
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.topic_view.view.*

/**
 * @author: jaffa
 * @date: 2019/8/11
 */
class InformationFragment : BaseFragment() {


    override fun epoxyController() = simpleController {

    }

    override fun initView() {

        recyclerView.setBackgroundColor(Color.parseColor("#f6f5fa"))

        val titles = arrayOf(
            resources.getString(R.string.today),
            resources.getString(R.string.currentWeek),
            resources.getString(R.string.currentMonth)
        )

        val fragments = ArrayList<Fragment>().also {
                it.add(TodayFragment())
                it.add(CurrentWeek())
                it.add(CurrentMonth())

        }

        //使用Viewstub添加布局
        titleViewStub.layoutResource = R.layout.information_view
        titleViewStub.inflate().apply {
            viewPager.adapter = ViewPagerAdapter(childFragmentManager, fragments, titles)
            viewPager.offscreenPageLimit = 4
            tabLayout.setViewPager(viewPager)
            viewPager.currentItem = 0
        }
    }
}