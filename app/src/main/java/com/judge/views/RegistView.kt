package com.judge.views

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ScrollView
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.TextProp
import com.judge.R
import com.judge.extensions.setTextIfDifferent
import kotlinx.android.synthetic.main.view_register.view.*

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

    private val userNameWatcher = SimpleTextWatcher{onUserNameChanged?.invoke(it)}
    private val passwordWatcher = SimpleTextWatcher{onPasswordChanged?.invoke(it)}
    private val confirmPasswordWatcher = SimpleTextWatcher{onConfirmPasswordChanged?.invoke(it)}
    private val emailWatcher = SimpleTextWatcher{onEmailChanged?.invoke(it)}
    private val phoneWatcher = SimpleTextWatcher{onPhoneChanged?.invoke(it)}
    private val messageWatcher = SimpleTextWatcher{onMessageChanged?.invoke(it)}
    private val codeWatcher = SimpleTextWatcher{onCodeChanged?.invoke(it)}

    init {
        inflate(context, R.layout.view_register, this)

        et_username.addTextChangedListener(userNameWatcher)
        et_password.addTextChangedListener(passwordWatcher)
        confirm_password.addTextChangedListener(confirmPasswordWatcher)
        et_email.addTextChangedListener(emailWatcher)
        et_phone.addTextChangedListener(phoneWatcher)
        et_message_code.addTextChangedListener(messageWatcher)
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
    fun setConfirmPassword(confPwd : CharSequence?){
        confirm_password.setTextIfDifferent(confPwd)
    }

    @TextProp
    fun setEmail(email : CharSequence?){
        et_email.setTextIfDifferent(email)
    }

    @TextProp
    fun setPhomeNumber(phone : CharSequence?){
        et_phone.setTextIfDifferent(phone)
    }

    @TextProp
    fun setMessageCode(messageCode : CharSequence?){
        et_message_code.setTextIfDifferent(messageCode)
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
    var onConfirmPasswordChanged : ((newText : String) -> Unit)? =null

    @set:CallbackProp
    var onEmailChanged : ((newText : String) -> Unit)? =null

    @set:CallbackProp
    var onPhoneChanged : ((newText : String) -> Unit)? =null

    @set:CallbackProp
    var onMessageChanged : ((newText : String) -> Unit)? =null

    @set:CallbackProp
    var onCodeChanged : ((newText : String) -> Unit)? =null

    @CallbackProp
    fun setBackClickListener(listener: OnClickListener?){
        register_back.setOnClickListener(listener)
    }

    @ModelProp(ModelProp.Option.IgnoreRequireHashCode)
    fun setPhoneCodeClickListener(listener: OnClickListener?){
        iv.setOnClickListener(listener)
    }

    @ModelProp(ModelProp.Option.IgnoreRequireHashCode)
    fun setCodeClickListener(listener: OnClickListener?){
        btn_get_code.setOnClickListener(listener)
    }

    @ModelProp(ModelProp.Option.IgnoreRequireHashCode)
    fun setSubmitClickListener(listener: OnClickListener?){
        reister_submit.setOnClickListener(listener)
    }

}


