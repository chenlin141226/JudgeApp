package com.judge.app.fragments.mine.setting

import android.os.Parcelable
import android.text.InputType
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.judge.R
import com.judge.app.core.MvRxEpoxyController
import com.judge.app.core.MvRxViewModel
import com.judge.app.core.simpleController
import com.judge.data.bean.SettingItemBean
import com.judge.data.repository.MineRepository
import com.judge.editTextView
import com.judge.extensions.copy
import com.judge.settingItem
import com.judge.utils.LogUtils
import com.judge.views.BottomPopupViewList
import com.judge.views.SimpleTextWatcher
import com.lxj.xpopup.interfaces.OnSelectListener
import com.vondear.rxtool.RxTool
import io.reactivex.schedulers.Schedulers
import kotlinx.android.parcel.Parcelize
import org.jetbrains.anko.collections.forEachWithIndex
import org.jetbrains.anko.support.v4.toast
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
    val privacyList: Array<String> =
        RxTool.getContext().resources.getStringArray(R.array.privacy_contents)
    val genderList: Array<String> =
        RxTool.getContext().resources.getStringArray(R.array.gender_list)

    fun getEditItems(index: Int) {
        when (index) {
            1 -> {
            }
            2 -> list.add(SettingItemBean(title = "性别", content = "男"))
            3 -> list.add(SettingItemBean(title = "生日", content = "2019-12-12"))
            4 -> list.add(SettingItemBean(title = "居住地", content = "广东 深圳"))
            5 -> {
            }
            6 -> {
            }
        }
        list.add(
            SettingItemBean(
                title = "隐私",
                content = "公开"
            )
        )
        setState {
            copy(items = list)
        }
    }

    fun updateItem(index: Int, bean: SettingItemBean, content: String) {
        setState {
            copy(items = items.copy(index, bean.copy(content = content)))
        }
    }

    fun setArgsValue(content: String) {
        setState {
            copy(items = items.copy(0, items[0].copy(content = content)))
        }
    }

    companion object : MvRxViewModelFactory<EditViewModel, EditState> {
        override fun create(viewModelContext: ViewModelContext, state: EditState): EditViewModel? {
            return EditViewModel(state)
        }
    }
}

class EditNameFragment : BaseEditFragment() {
    override fun epoxyController(): MvRxEpoxyController = simpleController(viewModel) { state ->
        editTextView {
            id("nameEdit")
            inputType(InputType.TYPE_CLASS_TEXT)
            item(state.settingArgs)
            textWatcher(SimpleTextWatcher {
                args.content = it
            })
        }
        state.items.forEachWithIndex { index, itemBean ->
            settingItem {
                id("name privacy$index")
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