package com.judge.app.fragments.judge

import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.airbnb.mvrx.fragmentViewModel
import com.judge.app.core.BaseFragment
import com.judge.app.core.MvRxViewModel
import com.judge.app.core.simpleController
import com.judge.data.bean.Forumlist
import com.judge.data.repository.JudgeRepository
import com.judge.editTextView
import com.judge.editionItem
import com.judge.utils.LogUtils
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.collections.forEachWithIndex

/**
 * @author: jaffa
 * @date: 2019/8/11
 * 主版
 */
data class EditionState(
    val editionItems: List<Forumlist>? = emptyList(),
    val isLoading: Boolean = false
) : MvRxState

class EditionViewModel(editionState: EditionState) : MvRxViewModel<EditionState>(editionState) {
    init {
        fetEditionData()
    }

    fun fetEditionData() = withState { state ->
        if (state.isLoading) return@withState

        JudgeRepository.getEdition().subscribeOn(Schedulers.io())
            .doOnSubscribe { setState { copy(isLoading = true) } }
            .doOnError { it.message?.let { it1 -> LogUtils.e(it1) } }
            .doFinally { setState { copy(isLoading = false) } }
            .execute { copy(editionItems = it()?.Variables?.forumlist ?: emptyList()) }
    }

    companion object : MvRxViewModelFactory<EditionViewModel, EditionState> {
        override fun create(viewModelContext: ViewModelContext, state: EditionState): EditionViewModel? {
            return EditionViewModel(state)
        }
    }

}

class EditionFragment : BaseFragment() {

    private val viewModel : EditionViewModel by fragmentViewModel()

    override fun epoxyController() = simpleController(viewModel) {state ->

        state.editionItems?.forEachWithIndex { index, item ->
              editionItem {
                  id(item.fid)
                  editionItem(item)
                  onClick { _, _, _, position ->

                  }
              }
        }

    }

    override fun initView() {
        refreshLayout.apply {
            setEnableAutoLoadMore(true)
            setEnableRefresh(true)
            setEnableLoadMore(true)
            setOnRefreshListener {
                viewModel.fetEditionData()
                it.finishRefresh(1000)
            }

            setOnLoadMoreListener {

                it.finishLoadMore(1000)
            }
        }
    }

}