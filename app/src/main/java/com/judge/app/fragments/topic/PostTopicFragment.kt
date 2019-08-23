package com.judge.app.fragments.topic

import android.text.InputType
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import com.judge.R
import com.judge.app.core.BaseFragment
import com.judge.app.core.simpleController
import com.judge.posttopicItem
import com.judge.views.SimpleTextWatcher
import org.jetbrains.anko.sdk27.coroutines.onClick

/**
 * @author: jaffa
 * @date: 2019/8/22
 * 签到
 */
class PostTopicFragment : BaseFragment(){
    override fun epoxyController() = simpleController {
        posttopicItem {
            id("post")
            inputType(InputType.TYPE_CLASS_TEXT)
           // item(state.settingArgs)
            textWatcher(SimpleTextWatcher {
                //args.content = it
            })
        }
    }

    override fun initView() {
        toolbar.isVisible = true
        rightButton.apply {
            text = resources.getString(R.string.publish)
            visibility = View.VISIBLE
            onClick {
                findNavController().navigate(R.id.action_postFragment_to_expressionFragment)
            }
        }
    }
}