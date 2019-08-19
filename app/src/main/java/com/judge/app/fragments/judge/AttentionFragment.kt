package com.judge.app.fragments.judge

import android.widget.Toast
import com.airbnb.mvrx.*
import com.judge.app.core.BaseFragment
import com.judge.app.core.MvRxViewModel
import com.judge.app.core.simpleController
import com.judge.attentionItem
import com.judge.data.bean.Attention
import com.judge.data.bean.AttentionBean
import com.judge.data.repository.JudgeRepository
import com.judge.utils.LogUtils
import com.vondear.rxtool.view.RxToast
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

class AttentionViewModel(initialState: AttentionState) : MvRxViewModel<AttentionState>(initialState) {
    private val list = mutableListOf<AttentionBean>()

    init {
        fetchAttention()
    }

    fun fetchAttention() = withState { state: AttentionState ->

        if(state.isLoading)return@withState

        JudgeRepository.getAttention().subscribeOn(Schedulers.io())
            .doOnSubscribe { setState { copy(isLoading = true) } }
            .doOnError { it.message.let { it1 -> LogUtils.e(it1!!) } }
            .doFinally { setState { copy(isLoading = false) } }
            .execute { copy(attentionItems = it()?.Variables?.list ?: emptyList())}
    }


    companion object : MvRxViewModelFactory<AttentionViewModel, AttentionState> {
        override fun create(viewModelContext: ViewModelContext, state: AttentionState): AttentionViewModel? {
            return AttentionViewModel(state)
        }
    }
}

class AttentionFragment : BaseFragment() {

    val viewmModel: AttentionViewModel by fragmentViewModel()

    override fun epoxyController() = simpleController(viewmModel) { state ->

        state.attentionItems.forEachWithIndex { index, item ->
            attentionItem {
                id(item.favid)
                attention(item)
                onParentClick { model, parentView, clickedView, position ->
                    context?.let { RxToast.info(it, "success", Toast.LENGTH_SHORT, true).show() }
                }
                onClick { _ ->
                    context?.let { RxToast.info(it, "success", Toast.LENGTH_SHORT, true).show() }
                }
            }
        }
    }


    override fun initData() {
    }
}