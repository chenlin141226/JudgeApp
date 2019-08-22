package com.judge.app.fragments.mine

import android.view.View
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.airbnb.mvrx.fragmentViewModel
import com.judge.R
import com.judge.app.core.BaseFragment
import com.judge.app.core.MvRxEpoxyController
import com.judge.app.core.MvRxViewModel
import com.judge.app.core.simpleController
import com.judge.data.bean.Topic
import com.judge.data.bean.TopicBean
import com.judge.data.repository.MineRepository
import com.judge.extensions.delete
import com.judge.topicItem
import com.judge.utils.LogUtils
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.collections.forEachWithIndex
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.toast
import java.util.*

data class FavoriteState(
    val topicItems: List<Topic> = emptyList(),
    val isLoading: Boolean = false
) : MvRxState

class FavoriteViewModel(
    initialState: FavoriteState
) : MvRxViewModel<FavoriteState>(initialState) {

    private val map = hashMapOf("version" to "4", "module" to "myfavthread", "start" to "0", "limit" to "20")

    init {
        fetchTopics()
    }

    fun fetchTopics() = withState { state ->
        if (state.isLoading) return@withState
        MineRepository.getPublishedTopics(map)
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                setState { copy(isLoading = true) }
            }
            .doOnError {
                it.message?.let { it1 -> LogUtils.e(it1) }
            }
            .doFinally { setState { copy(isLoading = false) } }
            .execute {
                copy(topicItems = topicItems.plus((it()?.Variables?.data ?: emptyList())))
            }
    }

    fun refreshTopics() {
        MineRepository.getPublishedTopics(map)
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                setState { copy(isLoading = true) }
            }
            .doOnError {
                it.message?.let { it1 -> LogUtils.e(it1) }
            }
            .doFinally { setState { copy(isLoading = false) } }
            .execute {
                copy(topicItems = (it()?.Variables?.data ?: emptyList()))
            }
    }

    fun deleteTopic(index: Int) {
        setState {
            copy(topicItems = topicItems.delete(index))
        }
    }

    companion object : MvRxViewModelFactory<FavoriteViewModel, FavoriteState> {
        override fun create(viewModelContext: ViewModelContext, state: FavoriteState): FavoriteViewModel? {
            return FavoriteViewModel(state)
        }
    }
}

class FavoriteFragment : BaseFragment() {
    private val viewModel: FavoriteViewModel by fragmentViewModel()
    override fun epoxyController(): MvRxEpoxyController = simpleController(viewModel) { state ->
        state.topicItems.forEachWithIndex { index, topicBean ->
            topicItem {
                id(topicBean.subject + index)
                topic(topicBean)
                onItemClick { _ ->
                    toast("You clicked item!")
                }
                onDeleteClick { _ ->
                    toast("You clicked delete button!")
                }
            }
        }
    }

    override fun initData() {
        super.initData()
        toolbar.visibility = View.VISIBLE
        rightButton.apply {
            visibility = View.VISIBLE
            text = getString(R.string.clear_all)
            onClick {

            }
        }
        viewModel.fetchTopics()
    }
}