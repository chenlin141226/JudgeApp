package com.judge.app.fragments.mine.topic

import android.view.View
import androidx.fragment.app.Fragment
import com.airbnb.mvrx.*
import com.judge.R
import com.judge.adapters.ViewPagerAdapter
import com.judge.app.core.BaseFragment
import com.judge.app.core.MvRxEpoxyController
import com.judge.app.core.MvRxViewModel
import com.judge.app.core.simpleController
import com.judge.data.bean.CommonResultBean
import com.judge.data.bean.Topic
import com.judge.data.repository.MineRepository
import com.judge.extensions.delete
import com.judge.network.JsonResponse
import com.judge.utils.LogUtils
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.topic_view.view.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class TopicFragment : BaseFragment() {

    override fun epoxyController(): MvRxEpoxyController = simpleController {
    }

    override fun initView() {
        super.initView()
        toolbar.visibility = View.VISIBLE
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