package com.judge.app.fragments.mine.setting

import android.text.InputType
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.airbnb.mvrx.fragmentViewModel
import com.jeremyliao.liveeventbus.LiveEventBus
import com.judge.R
import com.judge.app.core.BaseFragment
import com.judge.app.core.MvRxEpoxyController
import com.judge.app.core.simpleController
import com.judge.editTextView
import com.judge.settingItem
import com.judge.views.BottomPopupViewList
import com.judge.views.SimpleTextWatcher
import com.lxj.xpopup.interfaces.OnSelectListener
import org.jetbrains.anko.collections.forEachWithIndex
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.toast

class EditPhoneNumberFragment : BaseFragment() {
    private val viewModel: EditViewModel by fragmentViewModel()
    private lateinit var args: SettingArgs
    override fun epoxyController(): MvRxEpoxyController = simpleController(viewModel) { state ->
        args.index = state.settingArgs.index
        args.content = state.settingArgs.content
        editTextView {
            id("phoneNumberEdit")
            item(state.settingArgs)
            inputType(InputType.TYPE_CLASS_PHONE)
            textWatcher(SimpleTextWatcher{
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
                            if (position != viewModel.privacyList.size-1){
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
        viewModel.getEditItems(5)
        args = SettingArgs()
    }
}