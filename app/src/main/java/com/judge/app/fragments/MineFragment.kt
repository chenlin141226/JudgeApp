package com.judge.app.fragments

import android.content.res.TypedArray
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.airbnb.mvrx.fragmentViewModel
import com.judge.R
import com.judge.app.core.BaseFragment
import com.judge.app.core.MvRxEpoxyController
import com.judge.app.core.MvRxViewModel
import com.judge.app.core.simpleController
import com.judge.data.MineItemBean
import com.judge.extensions.copy
import com.judge.mineItem
import com.judge.mineTitle
import com.vondear.rxtool.RxTool
import java.util.*

data class MineItemState(
    val mineItems: List<MineItemBean> = emptyList()
) : MvRxState

class MineItemViewModel(
    initialState: MineItemState
) : MvRxViewModel<MineItemState>(initialState) {
    private val leftSelectedIcons: TypedArray =
        RxTool.getContext().resources.obtainTypedArray(R.array.mine_item_left_icons_selected)
    private val list = LinkedList<MineItemBean>()

    init {
        getItems()
    }

    private fun getItems() {
        val leftIcons = RxTool.getContext().resources.obtainTypedArray(R.array.mine_item_left_icons)
        val titles = RxTool.getContext().resources.getStringArray(R.array.mine_item_titles)
        titles.asIterable().forEachIndexed { index, title ->
            val item = MineItemBean(
                leftIconIdRes = leftIcons.getResourceId(index, 0),
                rightIconIdRes = R.drawable.icon_forward,
                text = title
            )
            list.add(index, item)
        }
        leftIcons.recycle()
        setState {
            copy(mineItems = list)
        }
    }

    fun updateIcon(index: Int, item: MineItemBean) {
        setState {
            copy(
                mineItems = list.copy(
                    index,
                    item.copy(
                        leftIconIdRes = leftSelectedIcons.getResourceId(index, 0)
                    )
                )
            )
        }
    }

    companion object : MvRxViewModelFactory<MineItemViewModel, MineItemState> {
        override fun create(viewModelContext: ViewModelContext, state: MineItemState): MineItemViewModel? {
            return MineItemViewModel(state)
        }
    }
}

class MineFragment : BaseFragment() {
    private val viewModel: MineItemViewModel by fragmentViewModel()

    override fun epoxyController(): MvRxEpoxyController = simpleController(viewModel) { state ->
        mineTitle {
            id("mine title")
        }
        state.mineItems.forEachIndexed { index, item ->
            mineItem {
                id(index)
                item(item)
                onClick { _ ->
                    viewModel.updateIcon(index, item)
                    navigateTo(R.id.action_mineFragment_to_whistleFragment, null)
                }
            }
        }
    }

}