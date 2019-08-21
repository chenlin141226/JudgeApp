package com.judge.app.fragments.mine

import android.content.res.TypedArray
import com.airbnb.mvrx.*
import com.judge.R
import com.judge.app.core.BaseFragment
import com.judge.app.core.MvRxEpoxyController
import com.judge.app.core.MvRxViewModel
import com.judge.app.core.simpleController
import com.judge.data.bean.ProfileBean
import com.judge.data.bean.MineItemBean
import com.judge.data.repository.MineRepository
import com.judge.extensions.copy
import com.judge.mineItem
import com.judge.mineTitle
import com.judge.utils.LogUtils
import com.vondear.rxtool.RxTool
import io.reactivex.schedulers.Schedulers
import java.util.*
import kotlin.collections.HashMap

data class MineItemState(
    val uerData: ProfileBean? = null,
    val mineItems: List<MineItemBean> = emptyList(),
    val isLoading: Boolean = false
) : MvRxState

class MineItemViewModel(
    initialState: MineItemState
) : MvRxViewModel<MineItemState>(initialState) {
    private val leftSelectedIcons: TypedArray =
        RxTool.getContext().resources.obtainTypedArray(R.array.mine_item_left_icons_selected)
    private val list = LinkedList<MineItemBean>()
    private val map = hashMapOf("version" to "4", "module" to "profile")

    init {
        getItems()
        getUserData(map)
    }

    private fun getUserData(map: HashMap<String, String>) = withState { state ->
        if (state.isLoading) return@withState
        MineRepository.getUserData(map).subscribeOn(Schedulers.io())
            .doOnSubscribe {
                setState { copy(isLoading = true) }
            }
            .doOnError {
                it.message?.let { it1 -> LogUtils.e(it1) }
            }
            .doFinally { setState { copy(isLoading = false) } }
            .execute {
                MineRepository.userProfile = it()?.Variables
                copy(uerData = it()?.Variables)
            }
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
        if (state.isLoading) {
            loadingDialog.show()
        } else {
            loadingDialog.dismiss()
        }
        mineTitle {
            id("mine title")
            photoUrl(state.uerData?.member_avatar)
            profile(state.uerData?.space)
            sign(state.uerData?.qiandaodb)
            uid("UID: " + state.uerData?.space?.uid)
        }
        state.mineItems.forEachIndexed { index, item ->
            mineItem {
                id(index)
                item(item)
                onClick { _ ->
                    when (index) {
                        0 -> navigateTo(R.id.action_mineFragment_to_messageFragment, null)
                        1 -> navigateTo(R.id.action_mineFragment_to_medalCenterFragment, null)
                        2 -> navigateTo(R.id.action_mineFragment_to_whistleFragment, null)
                        3 -> navigateTo(R.id.action_mineFragment_to_topicFragment, null)
                        4 -> navigateTo(R.id.action_mineFragment_to_favoriteFragment, null)
                        5 -> navigateTo(R.id.action_mineFragment_to_historyFragment, null)
                        6 -> navigateTo(R.id.action_mineFragment_to_settingFragment, null)
                        else -> navigateTo(R.id.action_mineFragment_to_settingFragment, null)
                    }
                    viewModel.updateIcon(index, item)
                }
            }
        }
    }

    override fun initView() {
        super.initView()
        sharedViewModel.setVisible(true)
    }
}