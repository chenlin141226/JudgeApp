package com.judge.views

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.widget.EditText
import android.widget.FrameLayout
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelView
import com.judge.R
import kotlinx.android.synthetic.main.view_findpsw.view.*

/**
 * @author: jaffa
 * @date: 2019/8/1
 */
@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_MATCH_HEIGHT)
class FindPwdView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val userNameWatcher = SimpleTextWatcher{onUserNameChanged?.invoke(it)}
    private val emailWatcher = SimpleTextWatcher{onEmailChanged?.invoke(it)}

    init {
        inflate(context, R.layout.view_findpsw, this)
        et_email.addTextChangedListener(emailWatcher)
        et_username.addTextChangedListener(userNameWatcher)
    }


    @set:CallbackProp
    var onUserNameChanged : ((newText : String) -> Unit)? =null

    @set:CallbackProp
    var onEmailChanged : ((newText : String) -> Unit)? =null

    @CallbackProp
    fun setBackCliclListener(listener: OnClickListener?){
        register_back.setOnClickListener(listener)
    }

    @CallbackProp
    fun setSubmitClickListener(listener: OnClickListener?){
        iv_register.setOnClickListener(listener)
    }

}

