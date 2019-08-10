package com.judge.app.fragments.mine.setting

import android.os.Parcelable
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.airbnb.mvrx.fragmentViewModel
import com.jeremyliao.liveeventbus.LiveEventBus
import com.judge.R
import com.judge.app.core.BaseFragment
import com.judge.app.core.MvRxEpoxyController
import com.judge.app.core.MvRxViewModel
import com.judge.app.core.simpleController
import com.judge.data.SettingItemBean
import com.judge.editTextView
import com.judge.settingItem
import kotlinx.android.parcel.Parcelize
import org.jetbrains.anko.collections.forEachWithIndex
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.util.*


@Parcelize
data class SettingArgs @JvmOverloads constructor(
    var index: Int = 1,
    var content: String = "",
    var privacy: String = ""
) : Parcelable

data class EditState(
    val settingArgs: SettingArgs,
    val items: List<SettingItemBean> = emptyList()
) : MvRxState {
    constructor(args: SettingArgs) : this(settingArgs = args)
}

class EditViewModel(
    initialState: EditState
) : MvRxViewModel<EditState>(initialState) {
    private val list = LinkedList<SettingItemBean>()

    init {
        getEditItems()
    }

    private fun getEditItems() {
        val item = SettingItemBean(
            title = "隐私",
            content = "公开"
        )
        list.add(item)
        setState {
            copy(items = list)
        }
    }

    companion object : MvRxViewModelFactory<EditViewModel, EditState> {
        override fun create(viewModelContext: ViewModelContext, state: EditState): EditViewModel? {
            return EditViewModel(state)
        }
    }
}

class EditNameFragment : BaseFragment() {
    private val viewModel: EditViewModel by fragmentViewModel()
    private lateinit var args: SettingArgs
    override fun epoxyController(): MvRxEpoxyController = simpleController(viewModel) { state ->
        editTextView {
            id("nameEdit")
            item(state.settingArgs)
            watcher {
                args.index = state.settingArgs.index
                args.content = it.toString()
            }
        }
        state.items.forEachWithIndex { index, itemBean ->
            settingItem {
                id("name privacy$index")
                item(itemBean)
                onClick { _ ->
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
        args = SettingArgs()
    }
}