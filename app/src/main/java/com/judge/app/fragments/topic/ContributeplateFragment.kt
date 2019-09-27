package com.judge.app.fragments.topic

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
import com.judge.app.core.MvRxViewModel
import com.judge.app.core.simpleController
import com.judge.data.bean.PlateBean
import com.judge.data.bean.PlateCategory
import com.judge.data.repository.JudgeRepository
import com.judge.extensions.clear
import com.judge.plateContentItem
import com.judge.plateItem
import com.judge.utils.LogUtils
import io.reactivex.schedulers.Schedulers
import kotlinx.android.parcel.Parcelize
import org.jetbrains.anko.collections.forEachWithIndex

/**
 * @author: jaffa
 * @date: 2019/9/6
 */
@Parcelize
data class PlateArgs @JvmOverloads constructor(
    var plateName: String = "",
    var formhash: String = "",
    var fid : String = "",
    var typeId : String =""
) : Parcelable

data class PlateState(
    val isLoading: Boolean = false,
    val plate: List<PlateCategory> = emptyList(),
    val attentionPlate: List<PlateCategory> = emptyList(),
    val recommendPlate: List<PlateCategory> = emptyList(),
    val variables: PlateBean? = null
) : MvRxState

class PlateViewModel(initialiState: PlateState) : MvRxViewModel<PlateState>(initialiState) {

    fun fetchPlate() {
        JudgeRepository.getPlate().subscribeOn(Schedulers.io())
            .doOnSubscribe { setState { copy(isLoading = true) } }
            .doOnError { it.message.let { it1 -> LogUtils.e(it1!!) } }
            .doFinally { setState { copy(isLoading = false) } }
            .execute {
                copy(
                    variables = it()?.Variables,
                    plate = it()?.Variables?.forumlist ?: emptyList(),
                    attentionPlate = it()?.Variables?.forumlist?.filterIndexed { _, plateCategory ->
                        plateCategory.favorite == "1"
                    } ?: emptyList(),
                    recommendPlate = it()?.Variables?.forumlist?.filterIndexed { _, plateCategory ->
                        plateCategory.favorite == "0"
                    } ?: emptyList()
                )
            }
    }

    //重置
    fun clearPlate() {
        setState {
            copy(
                plate = plate.clear(),
                attentionPlate = attentionPlate.clear(),
                recommendPlate = recommendPlate.clear()
            )
        }
    }

    companion object : MvRxViewModelFactory<PlateViewModel, PlateState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: PlateState
        ): PlateViewModel? {
            return PlateViewModel(state)
        }
    }
}

class ContributeplateFragment : BaseFragment() {

    val viewModel: PlateViewModel by fragmentViewModel()
    val plateArgs = PlateArgs()
    override fun epoxyController() = simpleController(viewModel) { state ->
        plateItem {
            id("attentions")
            title(resources.getString(R.string.attention_plate))
        }

        state.attentionPlate.forEachWithIndex { _, item ->
            plateContentItem {
                id(state.plate.size)
                item(item)
                onclick { _ ->
                    plateArgs.plateName = item.name
                    plateArgs.formhash = state.variables?.formhash.toString()
                    plateArgs.fid = item.fid
                    plateArgs.typeId = item.typeid
                    LiveEventBus.get().with("plate").post(plateArgs)
                    findNavController().navigateUp()
                }
            }
        }

        plateItem {
            id("recommend")
            title(resources.getString(R.string.recommend_plate))
        }

        state.recommendPlate.forEachWithIndex { _, item ->
            plateContentItem {
                id(state.plate.size)
                item(item)
                onclick { _ ->
                    plateArgs.plateName = item.name
                    plateArgs.formhash = state.variables?.formhash.toString()
                    plateArgs.fid = item.fid
                    LiveEventBus.get().with("plate").post(plateArgs)
                    findNavController().navigateUp()
                }
            }
        }
    }

    override fun initView() {
        super.initView()
        toolbar.isVisible = true
        toolbar.title = "投稿模板"


        viewModel.selectSubscribe(PlateState::isLoading) {
            if (it) {
                loadingDialog.show()
            } else {
                loadingDialog.dismiss()
            }
        }
        refreshLayout.apply {
            setEnableAutoLoadMore(true)
            setEnableRefresh(true)
            setEnableLoadMore(true)
            setOnRefreshListener {
                viewModel.fetchPlate()
                it.finishRefresh(1000)
            }

            setOnLoadMoreListener {
                it.finishLoadMore(1000)
            }
        }

    }

    override fun initData() {
        super.initData()
        viewModel.fetchPlate()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.clearPlate()
    }
}