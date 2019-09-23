package com.judge.app.fragments.topic

import android.text.InputType
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.airbnb.mvrx.*
import com.jeremyliao.liveeventbus.LiveEventBus
import com.judge.R
import com.judge.app.core.BaseFragment
import com.judge.app.core.MvRxViewModel
import com.judge.app.core.simpleController
import com.judge.data.bean.PushTie
import com.judge.data.repository.JudgeRepository
import com.judge.network.Message
import com.judge.sendtopicItem
import com.judge.views.SimpleTextWatcher
import com.vondear.rxtool.view.RxToast
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.runOnUiThread

/**
 * @author: jaffa
 * @date: 2019/8/22
 */
data class SendTopicState(
    val title: String = "",
    val content: String = "",
    val plate: String = "",
    val backPlate: String = "",
    val formhash: String = "",
    val pushTie : PushTie? =null,
    val message : Message? =null,
    val fid : String = ""
) : MvRxState

class SendTopicViewModel(initialState: SendTopicState) :
    MvRxViewModel<SendTopicState>(initialState) {

    fun updateArgs(args: PlateArgs) {
        setState { copy(backPlate = args.plateName,formhash = args.formhash,fid = args.fid)}
    }

    fun updateTitlet(title: String) {
        setState { copy(title = title) }
    }

    fun updateContent(content: String) {
        setState { copy(content = content) }
    }

    fun pushTie() = withState { state ->
        val map = hashMapOf("fid" to state.fid,"topicsubmit" to "1",
            "subject" to state.backPlate,"message" to state.title+state.content,"formhash" to state.formhash)
        JudgeRepository.pushTie(map).subscribeOn(Schedulers.io())
            .execute { copy(pushTie = it()?.Variables,message = it()?.Message) }
    }

    fun reset(){
        setState { copy(pushTie = null,message = null) }
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
            plate(state.backPlate)
            titleTextWatcher(SimpleTextWatcher { viewModel.updateTitlet(it) })

            contentTextWatcher(SimpleTextWatcher { viewModel.updateContent(it) })

            onclick { _ ->
                findNavController().navigate(R.id.action_signFragment_to_contributeplate)
            }

            if(state.pushTie?.code == "1"){
                viewModel.reset()
                context?.let {
                    RxToast.info(it, state.message?.messagestr.toString(), Toast.LENGTH_SHORT, false).show()
                }
                runOnUiThread { findNavController().popBackStack() }
            }else if(state.pushTie?.code == "0"){
                viewModel.reset()
                context?.let {
                    RxToast.info(it, state.message?.messagestr.toString(), Toast.LENGTH_SHORT, false).show()
                }
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
                withState(viewModel){state->
                    if(state.backPlate.isEmpty()){
                        context?.let {
                            RxToast.info(it,"请选择投稿板块", Toast.LENGTH_SHORT, false).show()
                        }
                        return@withState
                    }

                    if(state.title.isEmpty()){
                        context?.let {
                            RxToast.info(it,"请输入标题", Toast.LENGTH_SHORT, false).show()
                        }
                        return@withState
                    }

                    if(state.content.isEmpty()){
                        context?.let {
                            RxToast.info(it,"请输入内容", Toast.LENGTH_SHORT, false).show()
                        }
                        return@withState
                    }
                    viewModel.pushTie()
                }
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