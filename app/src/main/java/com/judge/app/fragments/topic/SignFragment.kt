package com.judge.app.fragments.topic

import android.text.InputType
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.airbnb.mvrx.fragmentViewModel
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
    val title : String? = "",
    val content : String? = "",
    val plate : String? = ""
):MvRxState

class SendTopicViewModel(initialState: SendTopicState) : MvRxViewModel<SendTopicState>(initialState) {

    fun updateTitlet(title :String){
        setState { copy(title = title) }
    }

    fun updateContent(content :String){
        setState { copy(content = content) }
    }

    companion object : MvRxViewModelFactory<SendTopicViewModel,SendTopicState>{
        override fun create(
            viewModelContext: ViewModelContext,
            state: SendTopicState
        ): SendTopicViewModel? {
            return SendTopicViewModel(state)
        }
    }
}


class SignFragment : BaseFragment() {

    val viewModel : SendTopicViewModel by fragmentViewModel()

    override fun epoxyController() = simpleController(viewModel) {state ->
           sendtopicItem {
               id("sendtopic")
               inputType(InputType.TYPE_CLASS_TEXT)
               title("")
               content("")
               titleTextWatcher(SimpleTextWatcher{viewModel.updateTitlet(it)})

               contentTextWatcher(SimpleTextWatcher{viewModel.updateContent(it)})

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

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.updateTitlet("")
        viewModel.updateContent("")
    }
}