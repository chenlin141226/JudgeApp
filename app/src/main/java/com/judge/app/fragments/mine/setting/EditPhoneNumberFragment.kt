package com.judge.app.fragments.mine.setting

import android.text.InputType
import com.judge.app.core.MvRxEpoxyController
import com.judge.app.core.simpleController
import com.judge.editTextView
import com.judge.settingItem
import com.judge.views.BottomPopupViewList
import com.judge.views.SimpleTextWatcher
import com.lxj.xpopup.interfaces.OnSelectListener
import org.jetbrains.anko.collections.forEachWithIndex
import org.jetbrains.anko.support.v4.toast

class EditPhoneNumberFragment : BaseEditFragment() {
    override fun epoxyController(): MvRxEpoxyController = simpleController(viewModel) { state ->
        editTextView {
            id("phoneNumberEdit")
            item(state.settingArgs)
            inputType(InputType.TYPE_CLASS_PHONE)
            textWatcher(SimpleTextWatcher {
                args.content = it
            })
        }
        state.items.forEachWithIndex { index, itemBean ->
            settingItem {
                id("phone privacy$index")
                item(itemBean)
                onClick { _ ->
                    BottomPopupViewList(context!!, viewModel.privacyList)
                        .setOnSelectListener(OnSelectListener { position, text ->
                            toast(text)
                            if (position != viewModel.privacyList.size - 1) {
                                viewModel.updateItem(index, itemBean, text)
                            }
                        }).showPopup()
                }
            }
        }
    }

}