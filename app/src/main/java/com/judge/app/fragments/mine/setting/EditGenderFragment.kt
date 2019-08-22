package com.judge.app.fragments.mine.setting

import com.judge.app.core.MvRxEpoxyController
import com.judge.app.core.simpleController
import com.judge.blankView
import com.judge.settingItem
import com.judge.views.BottomPopupViewList
import com.lxj.xpopup.interfaces.OnSelectListener
import org.jetbrains.anko.collections.forEachWithIndex
import org.jetbrains.anko.support.v4.toast

class EditGenderFragment : BaseEditFragment() {
    private lateinit var list: Array<String>
    override fun epoxyController(): MvRxEpoxyController = simpleController(viewModel) { state ->
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
                                when (text) {
                                    "男", "女" -> args.content = text
                                    else -> args.privacy = text
                                }
                                viewModel.updateItem(index, itemBean, text)
                            }
                        }).showPopup()
                }
            }
        }
    }
}