package com.judge.app.fragments.judge

import android.view.View
import com.airbnb.mvrx.fragmentViewModel
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

class JudgeFragment : BaseFragment() {

    private val videoViewModel: VideoViewModel by fragmentViewModel()

    override fun epoxyController(): MvRxEpoxyController = simpleController(videoViewModel) { state ->
        loadingView {
            id("loader")
            loading(state.isLoading)
        }
        state.videos?.forEach {
            videoRow {
                id(it.id)
                video(it)
                onClick { _ ->
                    RxDialogSureCancel(context).apply {
                        setContent("确定测试！")
                        sureView.setOnClickListener {
                            dismiss()
                            RxToast.showToast("you clicked confirm!")
                        }
                        cancelView.setOnClickListener {
                            dismiss()
                            RxToast.showToast("you clicked cancel!")
                        }
                    }.show()
                }
            }
        }
    }

    override fun setToolBar() {
        super.setToolBar()
        toolbar.visibility = View.VISIBLE
    }

    override fun initData() {
        videoViewModel.fetchVideos()
    }
}