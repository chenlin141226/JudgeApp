package com.judge.app.fragments.mine.setting

import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.airbnb.mvrx.fragmentViewModel
import com.jeremyliao.liveeventbus.LiveEventBus
import com.judge.R
import com.judge.app.core.BaseFragment
import org.jetbrains.anko.sdk27.coroutines.onClick

abstract class BaseEditFragment : BaseFragment() {
    protected val viewModel: EditViewModel by fragmentViewModel()
    protected lateinit var args: SettingArgs

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
        args = SettingArgs()
        viewModel.selectSubscribe(EditState::settingArgs) {
            args.index = it.index
            args.content = it.content
            viewModel.getEditItems(it.index)
            when (it.index) {
                2, 3, 4 -> viewModel.setArgsValue(it.content)
                else -> {
                }
            }

        }
    }
}