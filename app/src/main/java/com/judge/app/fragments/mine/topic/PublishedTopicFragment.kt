package com.judge.app.fragments.mine.topic


class PublishedTopicFragment : BaseTopicFragment() {
    override fun initData() {
        super.initData()
        viewModel.fetchPublishedTopics()
    }
}