package com.judge.app.fragments.judge

import android.graphics.Color
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.airbnb.mvrx.fragmentViewModel
import com.judge.app.core.BaseFragment
import com.judge.app.core.MvRxViewModel
import com.judge.app.core.simpleController
import com.judge.data.repository.JudgeRepository
import com.judge.extensions.clear
import com.judge.judgeCategoryDetailItem
import com.judge.utils.LogUtils
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.collections.forEachWithIndex

/**
 * @author: jaffa
 * @date: 2019/8/21
 */
class EssenceViewModel(initialiState: NewState) : MvRxViewModel<NewState>(initialiState) {


    fun fetchDetail(id : String) = withState { state ->
        if (state.isLoading) return@withState
        val map = hashMapOf("page" to "1", "fid" to id)
        JudgeRepository.getEssenceCategoryDetail(map).subscribeOn(Schedulers.io())
            .doOnSubscribe { setState { copy(isLoading = true) } }
            .doOnError { it.message.let { it1 -> LogUtils.e(it1!!) } }
            .doFinally { setState { copy(isLoading = false) } }
            .execute { copy(categoryDetails = it()?.Variables?.forum_threadlist ?: emptyList()) }
    }

    fun clearDatail() = withState { state ->
        state.categoryDetails.clear()
        setState { copy(categoryDetails = categoryDetails) }
    }

    companion object : MvRxViewModelFactory<EssenceViewModel, NewState> {

        override fun create(viewModelContext: ViewModelContext, state: NewState): EssenceViewModel? {
            return EssenceViewModel(state)
        }
    }
}

class EssenceCategoryDatailFragment(id: String) : BaseFragment() {
    private val viewModel : EssenceViewModel by fragmentViewModel()
    var id = id
    override fun epoxyController() = simpleController(viewModel) {state ->

        state.categoryDetails.forEachWithIndex { index, item ->
            judgeCategoryDetailItem {
                id(item.tid)
                viewmodel(item)
            }
        }

    }

    override fun initView() {
        recyclerView.setBackgroundColor(Color.WHITE)
        viewModel.fetchDetail(id)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.clearDatail()
    }

}