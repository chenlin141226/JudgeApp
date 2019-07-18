package com.judge.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.judge.R
import kotlinx.android.synthetic.main.loading_view.view.*

@ModelView(autoLayout = ModelView.Size.WRAP_WIDTH_WRAP_HEIGHT)
class LoadingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    init {
        View.inflate(context, R.layout.loading_view, this)
    }

    @ModelProp
    fun setLoading(loading: Boolean) {
        loadingAnimation.isVisible = loading
    }
}