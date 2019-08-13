package com.judge.app.fragments.mine.setting

import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.airbnb.mvrx.fragmentViewModel
import com.jeremyliao.liveeventbus.LiveEventBus
import com.judge.R
import com.judge.app.core.BaseFragment
import com.judge.app.core.MvRxEpoxyController
import com.judge.app.core.simpleController
import com.judge.blankView
import com.judge.settingItem
import com.judge.views.BottomPopupViewList
import com.lxj.xpopup.interfaces.OnSelectListener
import org.jetbrains.anko.collections.forEachWithIndex
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.toast

class EditGenderFragment : BaseFragment() {
    private val viewModel: EditViewModel by fragmentViewModel()
    private lateinit var args: SettingArgs
    private lateinit var list: Array<String>
    override fun epoxyController(): MvRxEpoxyController = simpleController(viewModel) { state ->
        args.index = state.settingArgs.index
        args.content = state.settingArgs.content
        blankView {
            id("blank view")
        }
        state.items.forEachWithIndex { index, itemBean ->
            settingItem {
                id("Gender privacy$index")
                item(itemBean)
                onClick { _ ->
                    list = when (index) {
                        0 -> viewModel.genderList
                        1 -> viewModel.privacyList
                        else -> emptyArray()
                    }
                    BottomPopupViewList(context!!, list)
                        .setOnSelectListener(OnSelectListener { position, text ->
                            toast(text)
                            if (position != list.size - 1) {
                                viewModel.updateItem(index, itemBean, text)
                            }
                        }).showPopup()
                }
            }
        }
    }

    override fun initView() {
        super.initView()
        toolbar.isVisible = true
        rightButton.apply {
            text = getString(R.string.complete)
            isVisible = true
            onClick {
                LiveEventBus.get()
                    .with("setting")
                    .post(args)
                findNavController().popBackStack()
            }
        }
    }

    override fun initData() {
        super.initData()
        viewModel.getEditItems(2)
        args = SettingArgs()
    }
}