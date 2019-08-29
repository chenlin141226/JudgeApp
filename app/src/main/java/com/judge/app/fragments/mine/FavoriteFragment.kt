package com.judge.app.fragments.mine

import android.view.View
import androidx.core.view.isVisible
import com.judge.R
import com.judge.app.fragments.mine.topic.BaseTopicFragment
import com.judge.app.fragments.mine.topic.TopicState
import org.jetbrains.anko.sdk27.coroutines.onClick

class FavoriteFragment : BaseTopicFragment() {

    override fun initView() {
        super.initView()
        toolbar.isVisible = true
        rightButton.apply {
            visibility = View.VISIBLE
            text = getString(R.string.clear_all)
            onClick {
                viewModel.deleteTopics(-1, "favorite")
            }
        }
    }

    override fun initData() {
        super.initData()
        viewModel.fetchFavoriteTopics()
        viewModel.asyncSubscribe(TopicState::deleteResult, onSuccess = {
            viewModel.deleteTopic()
        })
    }

    override fun deleteTopics(index: Int) {
        viewModel.deleteTopics(index, "favorite")
    }
}