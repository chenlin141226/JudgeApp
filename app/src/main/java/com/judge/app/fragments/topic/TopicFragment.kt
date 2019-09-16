package com.judge.app.fragments.topic


import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.judge.R
import com.judge.app.core.BaseFragment
import com.judge.app.core.simpleController
import kotlinx.android.synthetic.main.fragment_sendtopic.view.*

/**
 * @author: jaffa
 * @date: 2019/9/16
 */
class TopicFragment : BaseFragment() {
    override fun epoxyController() = simpleController { }

    override fun initView() {
        super.initView()
        toolbar.visibility = View.GONE

        sharedViewModel.setVisible(false)

        refreshLayout.visibility = View.GONE
        titleViewStub.layoutResource = R.layout.fragment_sendtopic
        titleViewStub.run {
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        }
        titleViewStub.inflate().apply {
            tv_sign.setOnClickListener {
                findNavController().navigate(R.id.action_sendTopicFragment_to_postFragment)
            }

            tv_post.setOnClickListener {
                findNavController().navigate(R.id.action_sendTopicFragment_to_signFragment)
            }

            btn_quit.setOnClickListener {
               activity?.onBackPressed()
            }
        }
    }
}