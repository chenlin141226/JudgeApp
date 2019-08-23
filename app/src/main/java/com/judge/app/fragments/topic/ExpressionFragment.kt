package com.judge.app.fragments.topic

import androidx.core.view.isVisible
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.airbnb.mvrx.fragmentViewModel
import com.judge.R
import com.judge.app.core.BaseFragment
import com.judge.app.core.MvRxViewModel
import com.judge.app.core.simpleController
import com.judge.data.bean.Smiley
import com.judge.data.repository.JudgeRepository
import com.judge.expressionItem
import com.judge.utils.LogUtils
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.collections.forEachWithIndex

/**
 * @author: jaffa
 * @date: 2019/8/22
 */
data class ExpressionState(
    val isLoading: Boolean = false,
    val expression: List<Smiley> = emptyList()
) : MvRxState

class ExpressionViewModel(initialState: ExpressionState) :
    MvRxViewModel<ExpressionState>(initialState) {

    init {
        fetchExpression()
    }

    fun fetchExpression() = withState { state ->
        JudgeRepository.getExpression().subscribeOn(Schedulers.io())
            .doOnSubscribe { setState { copy(isLoading = true) } }
            .doOnError { it.message.let { it1 -> LogUtils.e(it1!!) } }
            .doFinally { setState { copy(isLoading = false) } }
            .execute { copy(expression = it()?.Variables?.data?.smiley ?: emptyList()) }
    }

    companion object : MvRxViewModelFactory<ExpressionViewModel, ExpressionState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: ExpressionState
        ): ExpressionViewModel? {
            return ExpressionViewModel(state)
        }
    }

}

class ExpressionFragment : BaseFragment() {

    private val viewModel: ExpressionViewModel by fragmentViewModel()

    override fun epoxyController() = simpleController(viewModel) { state ->
        state.expression.forEachWithIndex { index, item ->
            expressionItem {
                id(item.id)
                item(item)
                onclick { _ ->

                }
            }
        }
    }

    override fun initView() {
        toolbar.isVisible = true
        toolbar.title = resources.getString(R.string.emoji)
    }

}