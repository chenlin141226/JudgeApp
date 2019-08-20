package com.judge.app.fragments.judge

import com.airbnb.mvrx.MvRxState
import com.judge.app.core.BaseFragment
import com.judge.app.core.simpleController
import com.judge.data.bean.ForumThreadlist

/**
 * @author: jaffa
 * @date: 2019/8/18
 */
data class  JudgeCategoryDetailState( val categoryDetails : List<ForumThreadlist> = emptyList()) :MvRxState


class JudgeCateGoryDetailFragment(index : Int) : BaseFragment() {

    override fun epoxyController() = simpleController {

    }
}