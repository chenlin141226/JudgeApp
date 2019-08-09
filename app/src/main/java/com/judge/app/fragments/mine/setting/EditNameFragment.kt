package com.judge.app.fragments.mine.setting

import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.judge.R
import com.judge.app.core.BaseFragment
import com.judge.app.core.MvRxEpoxyController
import com.judge.app.core.simpleController
import com.judge.data.SettingItemBean
import com.judge.editTextView
import com.judge.settingItem
import org.jetbrains.anko.sdk27.coroutines.onClick

class EditNameFragment : BaseFragment() {
    private var item = SettingItemBean(title = "隐私", content = "公开")
    override fun epoxyController(): MvRxEpoxyController = simpleController {
        editTextView {
            id("nameEdit")
            watcher {

            }
        }
        settingItem {
            id("name privacy")
            item(item)
            onClick { _ ->
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
                findNavController().popBackStack()
            }
        }
    }
}