package com.judge.app.fragments.topic

import android.view.View
import androidx.core.view.isVisible
import com.judge.R
import com.judge.app.core.BaseFragment
import com.judge.app.core.simpleController
import org.jetbrains.anko.sdk27.coroutines.onClick

/**
 * @author: jaffa
 * @date: 2019/8/22
 */
class PostTopicFragment : BaseFragment(){
    override fun epoxyController() = simpleController {  }

    override fun initView() {
        toolbar.isVisible = true
        rightButton.apply {
            text = resources.getString(R.string.publish)
            visibility = View.VISIBLE
            onClick {

            }
        }
    }
}