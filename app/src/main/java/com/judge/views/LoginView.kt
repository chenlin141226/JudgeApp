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
import com.vondear.rxui.view.RxCaptcha
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

        val spinnerData = resources.getStringArray(R.array.login_item_spinner)
        val spinnerAdapter = ArrayAdapter<String>(context,R.layout.login_spinner,spinnerData)
        spinner.adapter = spinnerAdapter
        //spinner.setSelection(0)

        RxCaptcha.build()
            .backColor(0xf9c660)
            .codeLength(6)
            .fontSize(50)
            .lineNumber(2)
            .size(180, 60)
            .type(RxCaptcha.TYPE.CHARS)
            .into(btn_get_code)

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

    /**
     * 下拉框选择
     */
    @CallbackProp
    fun setSpinnerItemSelectedListener(listener : AdapterView.OnItemSelectedListener?){
        spinner.onItemSelectedListener = listener
    }

    /**
     * 跳过登录
     */
    @CallbackProp
    fun setJumpClickListener(listener: OnClickListener?){
        loginJump.setOnClickListener(listener)
    }

    /**
     * 验证码点击
     */
    @CallbackProp
    fun setCodeClickListener(listener: OnClickListener?){
        btn_get_code.setOnClickListener(listener)
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