package com.judge.views

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ScrollView
import com.airbnb.epoxy.ModelView
import com.judge.R

/**
 * @author: jaffa
 * @date: 2019/8/1
 */
@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_MATCH_HEIGHT)
class RegistView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        ScrollView.inflate(context, R.layout.view_register, this)
    }

}


