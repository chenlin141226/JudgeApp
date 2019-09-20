package com.judge.app.fragments.topic

import android.annotation.SuppressLint
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
import com.judge.data.bean.SignResult
import com.judge.data.bean.SignResultBean
import com.judge.data.repository.JudgeRepository
import com.judge.posttopicItem
import com.judge.utils.LogUtils
import com.judge.views.SimpleTextWatcher
import com.vondear.rxtool.view.RxToast
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.runOnUiThread

/**-
 * @author: jaffa
 * @date: 2019/8/22
 * 签到
 */

data class PostTopicState(
    val content: String = "",
    val length: String = "0/20",
    val formhash: String = "",
    val qdxq: String = "",
    val gifUrl: String = "",
    val isLoading: Boolean = false,
    val sign: SignResultBean? = null,
    val result: SignResult? = null
) : MvRxState

class PostTopicViewModel(initialState: PostTopicState) :
    MvRxViewModel<PostTopicState>(initialState) {


    fun updataItem(args: ExpressionArgs) {
        setState { copy(gifUrl = args.gifUrl, formhash = args.formhash, qdxq = args.qdxq) }
    }

    //更新文字长度变化
    fun updateLength(str: String) {
        setState { copy(content = str) }
        setState { copy(length = "${str.length}/20") }
    }

    //发表
    fun pushContent() = withState { state: PostTopicState ->
        if (state.isLoading) return@withState

        val map = hashMapOf(
            "operation" to "qiandao",
            "todaysay" to state.content,
            "formhash" to state.formhash,
            "qdxq" to state.qdxq,
            "qdmode" to "1"
        )

        JudgeRepository.pushContent(map).subscribeOn(Schedulers.io())
            .doOnSubscribe { setState { copy(isLoading = true) } }
            .doOnError { it.message.let { it1 -> LogUtils.e(it1!!) } }
            .doFinally { setState { copy(isLoading = false) } }
            .execute { copy(result = it()?.Variables?.data, sign = it()?.Variables) }
    }

    fun reset() {
        setState { copy(result = null, sign = null) }
    }

    companion object : MvRxViewModelFactory<PostTopicViewModel, PostTopicState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: PostTopicState
        ): PostTopicViewModel? {
            return PostTopicViewModel(state)
        }
    }
}

class PostTopicFragment : BaseFragment() {
    private val viewModel: PostTopicViewModel by fragmentViewModel()
    override fun epoxyController() = simpleController(viewModel) { state ->

        posttopicItem {
            id("postTopic")
            inputType(InputType.TYPE_CLASS_TEXT)
            item(state.gifUrl)
            lenth(state.length)
            textWatcher(SimpleTextWatcher {
                viewModel.updateLength(it)
            })

            onclick { _ ->
                viewModel.updateLength("")
                findNavController().navigate(R.id.action_postFragment_to_expressionFragment)
            }


            if (state.result?.submit == "1") {
                context?.let {
                    RxToast.info(it, "签到成功", Toast.LENGTH_SHORT, false).show()
                }
                viewModel.reset()
                runOnUiThread { findNavController().popBackStack() }
            }else if(state.result?.submit == "0"){
                context?.let {
                    RxToast.info(it, state.result.msg, Toast.LENGTH_SHORT, false).show()
                }
                viewModel.reset()
            }
        }
    }

    override fun initData() {
        super.initData()
        LiveEventBus.get().with("expression", ExpressionArgs::class.java)
            .observe(this, Observer<ExpressionArgs> {
                viewModel.updataItem(it)
            })
    }


    @SuppressLint("ResourceAsColor")
    override fun initView() {
        toolbar.isVisible = true
        toolbar.title = resources.getString(R.string.post)
        rightButton.apply {
            text = resources.getString(R.string.publish)
            visibility = View.VISIBLE

            onClick {
                withState(viewModel) { state ->
                    if (state.gifUrl.isEmpty()) {
                        context?.let {
                            RxToast.info(it, "请选择心情图片", Toast.LENGTH_SHORT, false).show()
                        }
                        return@withState
                    }

                    if (state.content.isEmpty()) {
                        context?.let {
                            RxToast.info(it, "请发表内容", Toast.LENGTH_SHORT, false).show()
                        }
                        return@withState
                    }

                    viewModel.pushContent()
                }
            }
        }
    }
}