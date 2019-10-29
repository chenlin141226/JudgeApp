package com.judge.app.fragments.judge

import androidx.lifecycle.Observer
import com.airbnb.mvrx.*
import com.jeremyliao.liveeventbus.LiveEventBus
import com.judge.R
import com.judge.app.core.BaseFragment
import com.judge.app.core.MvRxViewModel
import com.judge.app.core.simpleController
import com.judge.app.fragments.topic.PlateArgs
import com.judge.attentionItem
import com.judge.data.bean.Attention
import com.judge.data.repository.JudgeRepository
import com.judge.utils.LogUtils
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.collections.forEachWithIndex

/**
 * @author: jaffa
 * @date: 2019/8/8
 * 关注
 */
data class AttentionState(
    val attentionItems: List<Attention> = emptyList(),
    val isLoading: Boolean = false
) : MvRxState

class AttentionViewModel(initialState: AttentionState) :
    MvRxViewModel<AttentionState>(initialState) {
    init {
        fetchAttention()
    }

    fun fetchAttention() = withState { state: AttentionState ->

        if (state.isLoading) return@withState

        JudgeRepository.getAttention().subscribeOn(Schedulers.io())
            .doOnSubscribe { setState { copy(isLoading = true) } }
            .doOnError { it.message.let { it1 -> LogUtils.e(it1!!) } }
            .doFinally { setState { copy(isLoading = false) } }
            .execute { copy(attentionItems = it()?.Variables?.list ?: emptyList()) }
    }


    companion object : MvRxViewModelFactory<AttentionViewModel, AttentionState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: AttentionState
        ): AttentionViewModel? {
            return AttentionViewModel(state)
        }
    }
}

class AttentionFragment : BaseFragment() {

    val viewModel: AttentionViewModel by fragmentViewModel()

    override fun epoxyController() = simpleController(viewModel) { state ->
        state.attentionItems.forEachWithIndex { _, item ->
            attentionItem {
                id(item.favid)
                attention(item)
            }
        }
    }


    override fun initView() {
        LiveEventBus.get().with("EditionFragment").observe(this, Observer{
            viewModel.fetchAttention()
        })
        sharedViewModel.setVisible(true)
        viewModel.selectSubscribe(AttentionState::isLoading) {
            if (it) {
                loadingDialog.show()
            } else {
                loadingDialog.dismiss()
            }
        }
        refreshLayout.apply {
            setEnableAutoLoadMore(true)
            setEnableRefresh(true)
            setEnableLoadMore(true)
            setOnRefreshListener {
                viewModel.fetchAttention()
                it.finishRefresh(1000)
            }

            setOnLoadMoreListener {

                it.finishLoadMore(1000)
            }
        }
    }
}