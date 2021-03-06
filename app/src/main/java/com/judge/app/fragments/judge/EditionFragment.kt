package com.judge.app.fragments.judge

import android.widget.Toast
import com.airbnb.mvrx.*
import com.jeremyliao.liveeventbus.LiveEventBus
import com.judge.R
import com.judge.app.core.BaseFragment
import com.judge.app.core.MvRxViewModel
import com.judge.app.core.simpleController
import com.judge.data.bean.Forumlist
import com.judge.data.bean.SubscribeBean
import com.judge.data.repository.JudgeRepository
import com.judge.editionItem
import com.judge.models.TopicState
import com.judge.network.JsonResponse
import com.judge.network.Message
import com.judge.utils.LogUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.collections.forEachWithIndex

/**
 * @author: jaffa
 * @date: 2019/8/11
 * 主版
 */
data class EditionState(val editionItems: List<Forumlist> = emptyList(),
                        val isLoading: Boolean = false,
                        val formhash: String? = null,
                        val subscribeBean: SubscribeBean? = null,
                        val subscribe: Async<JsonResponse<SubscribeBean>> = Uninitialized,
                        val message: Message? = null) : MvRxState

class EditionViewModel(editionState: EditionState) : MvRxViewModel<EditionState>(editionState) {
    init {
        fetEditionData()
    }
    fun fetEditionData() = withState { state ->
        if (state.isLoading) return@withState

        JudgeRepository.getEdition().subscribeOn(Schedulers.io())
            .doOnSubscribe { setState { copy(isLoading = true) } }
            .doOnError { it.message?.let { it1 -> LogUtils.e(it1) } }
            .doFinally { setState { copy(isLoading = false) } }.execute {
                copy(editionItems = it()?.Variables?.forumlist ?: emptyList(),
                    formhash = it()?.Variables?.formhash)
            }
    }

    //订阅
    fun SubscribeJudge(id: String) = withState { state ->

        val maps = hashMapOf("formhash" to state.formhash, "id" to id)
        JudgeRepository.subscribeJudge(maps).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { setState { copy(isLoading = true) } }
            .doOnError { it.message.let { it1 -> LogUtils.e(it1!!) } }
            .doFinally { setState { copy(isLoading = false) } }
            .execute { copy(subscribeBean = it()?.Variables, message = it()?.Message) }

    }


    companion object : MvRxViewModelFactory<EditionViewModel, EditionState> {
        override fun create(viewModelContext: ViewModelContext,
                            state: EditionState): EditionViewModel? {
            return EditionViewModel(state)
        }
    }

}

class EditionFragment : BaseFragment() {

    private val viewModel: EditionViewModel by fragmentViewModel()

    override fun epoxyController() = simpleController(viewModel) { state ->

        state.editionItems.forEachWithIndex { index, item ->

            editionItem {
                id(item.fid+"abcd")
                editionItem(item)
                onParentClick { _->
                    navigateTo(R.id.action_judgeFragment_to_judgeDetailFragment,item)
                }
                onClick { _ ->
                    LiveEventBus.get().with("EditionFragment").post("EditionFragment")
                    viewModel.SubscribeJudge(item.fid)
//                                        val maps = hashMapOf("formhash" to state.formhash, "id" to item.fid)
//                                        JudgeRepository.subscribeJudge(maps).subscribeOn(Schedulers.io())
//                                            .observeOn(AndroidSchedulers.mainThread()).subscribe {
//                                                Toast.makeText(context,it.Message.messagestr,Toast.LENGTH_SHORT).show()
//                                                viewModel.fetEditionData()
//                                            }

                }
            }
        }

    }



    override fun initView() {

        refreshLayout.apply {
            setEnableAutoLoadMore(true)
            setEnableRefresh(true)
            setEnableLoadMore(true)
            setOnRefreshListener {
                viewModel.fetEditionData()
                it.finishRefresh(1000)
            }

            setOnLoadMoreListener {

                it.finishLoadMore(1000)
            }
        }
    }

}