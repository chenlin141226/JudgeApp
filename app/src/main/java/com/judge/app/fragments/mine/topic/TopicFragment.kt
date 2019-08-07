package com.judge.app.fragments.mine.topic

import android.view.View
import androidx.fragment.app.Fragment
import com.judge.R
import com.judge.adapters.ViewPagerAdapter
import com.judge.app.core.BaseFragment
import com.judge.app.core.MvRxEpoxyController
import com.judge.app.core.simpleController
import kotlinx.android.synthetic.main.topic_view.view.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class TopicFragment : BaseFragment() {

    override fun epoxyController(): MvRxEpoxyController = simpleController {
    }

    override fun initView() {
        super.initView()
        toolbar.visibility = View.VISIBLE
        toolbar.visibility = View.VISIBLE
        rightButton.apply {
            visibility = View.VISIBLE
            text = getString(R.string.clear_all)
            onClick {

            }
        }
        val titles = arrayOf(getString(R.string.published_topic), getString(R.string.replied_topic))
        val fragments = ArrayList<Fragment>().also {
            it.add(PublishedTopicFragment())
            it.add(RepliedTopicFragment())
        }
        titleViewStub.layoutResource = R.layout.topic_view
        titleViewStub.inflate().apply {
            viewPager.adapter = ViewPagerAdapter(childFragmentManager, fragments, titles)
            tabLayout.setViewPager(viewPager)
        }
    }
}