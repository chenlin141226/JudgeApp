package com.judge.app.fragments.mine

import android.view.View
import androidx.core.view.isVisible
import com.judge.R
import com.judge.app.fragments.mine.topic.BaseTopicFragment
import org.jetbrains.anko.sdk27.coroutines.onClick


class HistoryFragment : BaseTopicFragment() {
    override fun initView() {
        super.initView()
        toolbar.isVisible = true
        rightButton.apply {
            visibility = View.VISIBLE
            text = getString(R.string.clear_all)
            onClick {
                viewModel.deleteTopics(-1, "history")
            }
        }
    }

    override fun initData() {
        super.initData()
        viewModel.fetchHistoryTopics()
        viewModel.setSwipeEnable(true)
    }

    override fun deleteTopics(index: Int) {
        viewModel.deleteTopics(index, "history")
    }
}