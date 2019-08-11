package com.judge.app.fragments.judge

import android.graphics.Color
import android.widget.Toast
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.airbnb.mvrx.fragmentViewModel
import com.judge.app.core.BaseFragment
import com.judge.app.core.MvRxViewModel
import com.judge.app.core.simpleController
import com.judge.data.bean.RecommendBean
import com.judge.recommendItem
import com.vondear.rxtool.view.RxToast
import org.jetbrains.anko.collections.forEachWithIndex

/**
 * @author: jaffa
 * @date: 2019/8/11
 * 推荐
 */
data class RecommendState(
    val recommendItems: List<RecommendBean> = emptyList()
) : MvRxState

class RecommendViewModel(initialState: RecommendState) : MvRxViewModel<RecommendState>(initialState) {
    private val list = mutableListOf<RecommendBean>()

    init {
        fetchRecommend()
    }

     fun fetchRecommend() {
        for (i in 1..9) {
            val recommend = RecommendBean(
                info = "我等了一个不该等的人，我又拿什么把伤口抚平",
                time = "03-18"
            )
            list.add(recommend)
        }
        setState { copy(recommendItems = list) }
    }

     fun removeRecommend() {
        list.clear()
        setState { copy(recommendItems = list) }
    }


    companion object : MvRxViewModelFactory<RecommendViewModel, RecommendState> {
        override fun create(viewModelContext: ViewModelContext, state: RecommendState): RecommendViewModel? {
            return RecommendViewModel(state)
        }
    }
}

class RecommendFragment : BaseFragment(){

    private val viewModel : RecommendViewModel by fragmentViewModel()

    override fun epoxyController() = simpleController(viewModel) {state ->

        state.recommendItems.forEachWithIndex { idnex, item ->

              recommendItem {
                  id(item.info + idnex)
                  recommendBean(item)
                  onParentClick { _ ->
                      context?.let { RxToast.info(it, "success", Toast.LENGTH_SHORT, true).show() }
                  }
              }
        }

    }

    override fun initData() {
        viewModel.fetchRecommend()
    }

    override fun initView() {
        recyclerView.setBackgroundColor(Color.parseColor("#f6f5fa"))
    }

    override fun onDestroyView() {
        viewModel.removeRecommend()
        super.onDestroyView()
    }
}