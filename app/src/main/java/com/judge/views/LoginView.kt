package com.judge.views

import android.content.Context
import android.text.Editable
import android.text.Spanned
import android.text.TextWatcher
import android.util.AttributeSet
import android.widget.EditText
import android.widget.FrameLayout
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.TextProp
import com.judge.R
import kotlinx.android.synthetic.main.view_login.view.*

/**
 * @author: jaffa
 * @date: 2019/7/28
 * 登录view
 */
@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_MATCH_HEIGHT)
class LoginView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val userNameWatcher = SimpleTextWatcher{onUserNameChanged?.invoke(it)}
    private val passwordWatcher = SimpleTextWatcher{onPasswordChanged?.invoke(it)}
    private val codeWatcher = SimpleTextWatcher { onCodeChanged?.invoke(it) }

    init {
        inflate(context, R.layout.view_login, this)
        et_username.addTextChangedListener(userNameWatcher)
        et_password.addTextChangedListener(passwordWatcher)
        et_code.addTextChangedListener(codeWatcher)
    }

    @TextProp
    fun setUserName(username : CharSequence?){
        et_username.setTextIfDifferent(username)
    }

    @TextProp
    fun setPassword(password : CharSequence?){
        et_password.setTextIfDifferent(password)
    }

    @TextProp
    fun setCode(code : CharSequence?){
        et_code.setTextIfDifferent(code)
    }


    @set:CallbackProp
    var onUserNameChanged : ((newText : String) -> Unit)? =null

    @set:CallbackProp
    var onPasswordChanged : ((newText : String) -> Unit)? =null

    @set:CallbackProp
    var onCodeChanged : ((newText : String) -> Unit)? =null

    /**
     * 登录的点击事件
     */
    @CallbackProp
    fun setClickListener(listener : OnClickListener?){
        iv_login.setOnClickListener(listener)
    }

}

/**
 * Set the given text on the textview.
 *
 * @return True if the given text is different from the previous text on the textview.
 */
fun EditText.setTextIfDifferent(newText: CharSequence?): Boolean {
    if (!isTextDifferent(newText, text)) {
        // Previous text is the same. No op
        return false
    }

    setText(newText)
    // Since the text changed we move the cursor to the end of the new text.
    // This allows us to fill in text programmatically with a different value,
    // but if the user is typing and the view is rebound we won't lose their cursor position.
    setSelection(newText?.length ?: 0)
    return true
}

/**
 * @return True if str1 is different from str2.
 *
 *
 * This is adapted from how the Android DataBinding library binds its text views.
 */
fun isTextDifferent(str1: CharSequence?, str2: CharSequence?): Boolean {
    if (str1 === str2) {
        return false
    }
    if (str1 == null || str2 == null) {
        return true
    }
    val length = str1.length
    if (length != str2.length) {
        return true
    }

    if (str1 is Spanned) {
        return str1 != str2
    }

    for (i in 0 until length) {
        if (str1[i] != str2[i]) {
            return true
        }
    }
    return false
}

private class SimpleTextWatcher(val onTextChanged : (newText : String) -> Unit) : TextWatcher{
    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        onTextChanged(s.toString())
    }

}