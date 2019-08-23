package com.judge.app.fragments.mine.topic


class RepliedTopicFragment : BaseTopicFragment() {
    override fun initData() {
        super.initData()
        viewModel.fetchRepliedTopics()
    }
}