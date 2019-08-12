package com.judge.app.fragments.judge

import android.widget.Toast
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.airbnb.mvrx.fragmentViewModel
import com.judge.app.core.BaseFragment
import com.judge.app.core.MvRxViewModel
import com.judge.app.core.simpleController
import com.judge.attentionItem
import com.judge.data.bean.AttentionBean
import com.vondear.rxtool.view.RxToast
import org.jetbrains.anko.collections.forEachWithIndex

/**
 * @author: jaffa
 * @date: 2019/8/8
 * 关注
 */
data class AttentionState(
    val attentionItems: List<AttentionBean> = emptyList()
) : MvRxState

class AttentionViewModel(initialState: AttentionState) : MvRxViewModel<AttentionState>(initialState) {
    private val list = mutableListOf<AttentionBean>()

    init {
        fetchAttention()
    }

    fun fetchAttention() {
        for (i in 1 .. 9) {
            val market = AttentionBean(
                attentionUrl = "https://i.redd.it/nbju2rir9xp11.jpg",
                attentionName = "小鱼儿$i",
                attentionNumber = "1314",
                attentionInfo = "我等了一个不该等的人，我又拿什么把伤口抚平"
            )
            list.add(market)
        }
        setState { copy(attentionItems = list) }
    }

    fun removeAttention() {
        list.clear()
        setState { copy(attentionItems = list) }
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
                id(item.attentionName+index)
                attentionBean(item)
                onParentClick { model, parentView, clickedView, position ->
                    context?.let { RxToast.info(it, "success", Toast.LENGTH_SHORT, true).show() }
                }
                onClick { _ ->
                    context?.let { RxToast.info(it, "success", Toast.LENGTH_SHORT, true).show() }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewmModel.removeAttention()
    }

    override fun initData() {
        viewmModel.fetchAttention()
    }
}