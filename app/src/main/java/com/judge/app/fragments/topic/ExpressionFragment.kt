package com.judge.app.fragments.topic

import android.annotation.SuppressLint
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
import com.judge.data.bean.EpressionBean
import com.judge.data.bean.Smiley
import com.judge.data.repository.JudgeRepository
import com.judge.expressionItem
import com.judge.utils.LogUtils
import io.reactivex.schedulers.Schedulers
import kotlinx.android.parcel.Parcelize
import org.jetbrains.anko.collections.forEachWithIndex

/**
 * @author: jaffa
 * @date: 2019/8/22
 */
@Parcelize
data class ExpressionArgs @JvmOverloads constructor(
    var gifUrl: String = "",
    var formhash : String ="",
    var qdxq : String = ""
) : Parcelable

data class ExpressionState(
    val isLoading: Boolean = false,
    val expression: List<Smiley> = emptyList(),
    val variables : EpressionBean? = null
) : MvRxState

class ExpressionViewModel(initialState: ExpressionState) :
    MvRxViewModel<ExpressionState>(initialState) {

    init {
        fetchExpression()
    }

    fun fetchExpression() = withState { _ ->
        JudgeRepository.getExpression().subscribeOn(Schedulers.io())
            .doOnSubscribe { setState { copy(isLoading = true) } }
            .doOnError { it.message.let { it1 -> LogUtils.e(it1!!) } }
            .doFinally { setState { copy(isLoading = false) } }
            .execute { copy(expression = it()?.Variables?.data?.smiley ?: emptyList(),variables = it()?.Variables) }
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
    val args = ExpressionArgs()

    override fun epoxyController() = simpleController(viewModel) { state ->
        state.expression.forEachWithIndex { _, item ->
            expressionItem {
                id(item.id)
                item(item)
                onclick { _ ->
                    args.gifUrl = item.emoticon
                    args.qdxq = item.qdxq
                    args.formhash = state.variables?.formhash.toString()
                    LiveEventBus.get().with("expression").post(args)
                    findNavController().navigateUp()
                }
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    override fun initView() {
        toolbar.isVisible = true
        toolbar.title = resources.getString(R.string.emoji)
        toolbar.setTitleTextColor(R.color.colorAccent)
    }

}