package com.judge.app.fragments.mine.medal

import androidx.core.view.isVisible
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.airbnb.mvrx.fragmentViewModel
import com.judge.R
import com.judge.app.core.BaseFragment
import com.judge.app.core.MvRxEpoxyController
import com.judge.app.core.MvRxViewModel
import com.judge.app.core.simpleController
import com.judge.data.Medal
import com.judge.data.repository.MineRepository
import com.judge.medalItemView
import com.judge.utils.LogUtils
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.collections.forEachWithIndex
import org.jetbrains.anko.sdk27.coroutines.onClick

data class MedalState(
    val isLoading: Boolean = false,
    val medals: List<Medal>? = emptyList()
) : MvRxState

class MedalViewModel(
    initialState: MedalState
) : MvRxViewModel<MedalState>(initialState) {
    private val map = hashMapOf("version" to "4", "module" to "medals")

    init {
        fetchMedals()
    }

    private fun fetchMedals() = withState { state ->
        if (state.isLoading) return@withState
        MineRepository.getMedals(map).subscribeOn(Schedulers.io())
            .doOnSubscribe {
                setState { copy(isLoading = true) }
            }
            .doOnError {
                it.message?.let { it1 -> LogUtils.e(it1) }
            }
            .doFinally { setState { copy(isLoading = false) } }
            .execute {
                copy(medals = it()?.Variables?.data)
            }
    }

    companion object : MvRxViewModelFactory<MedalViewModel, MedalState> {
        override fun create(viewModelContext: ViewModelContext, state: MedalState): MedalViewModel? {
            return MedalViewModel(state)
        }
    }
}

class MedalCenterFragment : BaseFragment() {
    private val viewModel: MedalViewModel by fragmentViewModel()
    override fun epoxyController(): MvRxEpoxyController = simpleController(viewModel) { state ->
        if (state.isLoading) {
            loadingDialog.show()
        } else {
            loadingDialog.dismiss()
        }
        state.medals?.forEachWithIndex { index, medal ->
            medalItemView {
                id(medal.name + index)
                medal(medal)
            }
        }
    }

    override fun initView() {
        super.initView()
        toolbar.isVisible = true
        rightButton.apply {
            isVisible = true
            text = getString(R.string.mine)
            onClick {
                navigateTo(R.id.action_medalCenterFragment_to_mineMedalFragment, null)
            }
        }
    }

}