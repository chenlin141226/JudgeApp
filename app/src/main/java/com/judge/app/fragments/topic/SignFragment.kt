package com.judge.app.fragments.topic

import android.text.InputType
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
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
import com.judge.sendtopicItem
import com.judge.views.SimpleTextWatcher
import org.jetbrains.anko.sdk27.coroutines.onClick

/**
 * @author: jaffa
 * @date: 2019/8/22
 */
data class SendTopicState(
    val title: String? = "",
    val content: String? = "",
    val plate: String? = "",
    val backPlate: String? = "",
    val formhash: String = ""
) : MvRxState

class SendTopicViewModel(initialState: SendTopicState) :
    MvRxViewModel<SendTopicState>(initialState) {

    fun updateArgs(args: PlateArgs) {
        setState { copy(backPlate = args.plateName,formhash = args.formhash)}
    }

    fun updateTitlet(title: String) {
        setState { copy(title = title) }
    }

    fun updateContent(content: String) {
        setState { copy(content = content) }
    }

    fun pushTie() = withState { state ->

    }

    companion object : MvRxViewModelFactory<SendTopicViewModel, SendTopicState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: SendTopicState
        ): SendTopicViewModel? {
            return SendTopicViewModel(state)
        }
    }
}


class SignFragment : BaseFragment() {

    val viewModel: SendTopicViewModel by fragmentViewModel()

    override fun epoxyController() = simpleController(viewModel) { state ->
        sendtopicItem {
            id("sendtopic")
            inputType(InputType.TYPE_CLASS_TEXT)
            title(state.title)
            content(state.content)
            titleTextWatcher(SimpleTextWatcher { viewModel.updateTitlet(it) })

            contentTextWatcher(SimpleTextWatcher { viewModel.updateContent(it) })

            onclick { _ ->
                findNavController().navigate(R.id.action_signFragment_to_contributeplate)
            }
        }
    }

    override fun initView() {
        toolbar.isVisible = true
        toolbar.title = resources.getString(R.string.sign)
        rightButton.apply {
            text = resources.getString(R.string.publish)
            visibility = View.VISIBLE
            onClick {
                viewModel.pushTie()
            }
        }
    }

    override fun initData() {
        super.initData()
        LiveEventBus.get().with("plate", PlateArgs::class.java).observe(this, Observer<PlateArgs> {
            viewModel.updateArgs(it)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.updateTitlet("")
        viewModel.updateContent("")
    }
}