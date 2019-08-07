package com.judge.app.fragments.mine

import androidx.core.view.isVisible
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.airbnb.mvrx.fragmentViewModel
import com.judge.R
import com.judge.app.core.BaseFragment
import com.judge.app.core.MvRxEpoxyController
import com.judge.app.core.MvRxViewModel
import com.judge.app.core.simpleController
import com.judge.data.SettingItemBean
import com.judge.settingItem
import com.judge.settingTitle
import com.vondear.rxtool.RxTool
import org.jetbrains.anko.collections.forEachWithIndex
import java.util.*


data class SettingState(
    val SettingItems: List<SettingItemBean> = emptyList()
) : MvRxState

class SettingViewModel(
    initialState: SettingState
) : MvRxViewModel<SettingState>(initialState) {
    private val list = LinkedList<SettingItemBean>()

    init {
        getSettingTitles()
    }

    private fun getSettingTitles() {
        val titles = RxTool.getContext().resources.getStringArray(R.array.setting_item_title)
        titles.asIterable().forEachIndexed { _, name ->
            val item = SettingItemBean(
                title = name
            )
            list.add(item)
        }

        setState {
            copy(SettingItems = list)
        }
    }

    companion object : MvRxViewModelFactory<SettingViewModel, SettingState> {
        override fun create(viewModelContext: ViewModelContext, state: SettingState): SettingViewModel? {
            return SettingViewModel(state)
        }
    }
}

class SettingFragment : BaseFragment() {
    val viewModel: SettingViewModel by fragmentViewModel()
    override fun epoxyController(): MvRxEpoxyController = simpleController(viewModel) { state ->
        state.SettingItems.forEachWithIndex { index, item ->
            if (index == 0) {
                settingTitle {
                    id(item.title + index)
                    item(item)
                }
            } else {
                settingItem {
                    id(item.title + index)
                    item(item)
                }
            }
        }
    }

    override fun initView() {
        super.initView()
        toolbar.isVisible = true
    }
}