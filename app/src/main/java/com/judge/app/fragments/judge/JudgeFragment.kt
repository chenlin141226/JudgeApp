package com.judge.app.fragments.judge

import android.view.View
import androidx.fragment.app.Fragment
import com.airbnb.mvrx.fragmentViewModel
import com.judge.R
import com.judge.adapters.ViewPagerAdapter
import com.judge.app.core.BaseFragment
import com.judge.app.core.MvRxEpoxyController
import com.judge.app.core.simpleController
import com.judge.models.VideoViewModel
import com.judge.videoRow
import com.judge.views.loadingView
import com.vondear.rxtool.view.RxToast
import com.vondear.rxui.view.dialog.RxDialog
import com.vondear.rxui.view.dialog.RxDialogSure
import com.vondear.rxui.view.dialog.RxDialogSureCancel
import kotlinx.android.synthetic.main.topic_view.view.*

class JudgeFragment : BaseFragment() {

    private val videoViewModel: VideoViewModel by fragmentViewModel()

    override fun epoxyController(): MvRxEpoxyController = simpleController(videoViewModel) { state ->
//        loadingView {
//            id("loader")
//            loading(state.isLoading)
//        }
//        state.videos?.forEach {
//            videoRow {
//                id(it.id)
//                video(it)
//                onClick { _ ->
//                    RxDialogSureCancel(context).apply {
//                        setContent("确定测试！")
//                        sureView.setOnClickListener {
//                            dismiss()
//                            RxToast.showToast("you clicked confirm!")
//                        }
//                        cancelView.setOnClickListener {
//                            dismiss()
//                            RxToast.showToast("you clicked cancel!")
//                        }
//                    }.show()
//                }
//            }
//        }
    }

    override fun initView() {

        val titles = arrayOf(
            resources.getString(R.string.attention),
            resources.getString(R.string.plate),
            resources.getString(R.string.recommend),
            resources.getString(R.string.information)
        )

        val fragments = ArrayList<Fragment>().also {
            for (index in 0 until 4) {
                it.add(AttentionFragment())
            }
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
        //videoViewModel.fetchVideos()
    }
}