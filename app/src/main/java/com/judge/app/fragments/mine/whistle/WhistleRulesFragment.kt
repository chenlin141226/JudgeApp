package com.judge.app.fragments.mine.whistle

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
import com.judge.data.bean.WhistleRulesBean
import com.judge.whistleRulesItem
import com.vondear.rxtool.RxTool
import org.jetbrains.anko.collections.forEachWithIndex
import java.util.*

data class WhistleRulesState(
    val whistleRules: List<WhistleRulesBean> = emptyList()
) : MvRxState

class WhistleRulesViewModel(
    initialState: WhistleRulesState
) : MvRxViewModel<WhistleRulesState>(initialState) {
    private val list = LinkedList<WhistleRulesBean>()

    init {
        setWhistlesRules()
    }

    private fun setWhistlesRules() {
        val actionNames = RxTool.getContext().resources.getStringArray(R.array.whistle_rule_name)
        val periods = RxTool.getContext().resources.getStringArray(R.array.whistle_rule_period)
        val rewardTimes = RxTool.getContext().resources.getStringArray(R.array.whistle_rule_reward_time)
        val rewardContents = RxTool.getContext().resources.getStringArray(R.array.whistle_rule_reward_content)

        actionNames.asList().forEachWithIndex { index, name ->
            val item = WhistleRulesBean(
                actionName = name,
                period = periods[index],
                rewardTime = rewardTimes[index],
                rewardContent = rewardContents[index]
            )
            list.add(item)
        }

        setState {
            copy(whistleRules = list)
        }
    }

    companion object : MvRxViewModelFactory<WhistleRulesViewModel, WhistleRulesState> {
        override fun create(viewModelContext: ViewModelContext, state: WhistleRulesState): WhistleRulesViewModel? {
            return WhistleRulesViewModel(state)
        }
    }
}

class WhistleRulesFragment : BaseFragment() {
    private val viewModel: WhistleRulesViewModel by fragmentViewModel()
    override fun epoxyController(): MvRxEpoxyController = simpleController(viewModel) { state ->
        state.whistleRules.forEachWithIndex { index, item ->
            whistleRulesItem {
                id(item.actionName + index)
                ruleItem(item)
            }
        }
    }

    override fun initView() {
        super.initView()
        toolbar.visibility = View.VISIBLE
        titleViewStub.layoutResource = R.layout.whistle_rules_title
        titleViewStub.inflate()
    }
}