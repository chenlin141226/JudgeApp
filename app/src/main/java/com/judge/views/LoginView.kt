package com.judge.views

import android.content.Context
import android.text.Editable
import android.text.Spanned
import android.text.TextWatcher
import android.util.AttributeSet
import android.widget.AdapterView
import android.widget.ArrayAdapter
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

        val spinnerData = resources.getStringArray(R.array.login_item_spinner)
        et_username.addTextChangedListener(userNameWatcher)
        et_password.addTextChangedListener(passwordWatcher)
        et_code.addTextChangedListener(codeWatcher)

        val spinnerAdapter = ArrayAdapter<String>(context,R.layout.login_spinner,spinnerData)
        spinner.adapter = spinnerAdapter
        //spinner.setSelection(0)
    }

    @TextProp
    fun setUserName(username : CharSequence?){
       et_username.setTextIfDifferent(username)
    }

    @TextProp
    fun setPassword(password : CharSequence?){

    }

    @TextProp
    fun setCode(code : CharSequence?){

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

    @CallbackProp
    fun setSpinnerItemSelectedListener(listener : AdapterView.OnItemSelectedListener?){
        spinner.onItemSelectedListener = listener
    }

    @CallbackProp
    fun setJumpClickListener(listener: OnClickListener?){
        loginJump.setOnClickListener(listener)
    }

}

fun EditText.setTextIfDifferent(newText: CharSequence?): Boolean {
    setText(newText)
    setSelection(newText?.length ?: 0)
    return true
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