package com.judge.views

import android.content.Context
import android.graphics.Bitmap
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.TextProp
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.judge.R
import com.judge.extensions.setTextIfDifferent
import com.judge.network.Constant
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

    private val userNameWatcher = SimpleTextWatcher { onUserNameChanged?.invoke(it) }
    private val passwordWatcher = SimpleTextWatcher { onPasswordChanged?.invoke(it) }
    private val codeWatcher = SimpleTextWatcher { onCodeChanged?.invoke(it) }
    private val questionWatcher = SimpleTextWatcher { onQuestionChanged?.invoke(it) }

    init {
        inflate(context, R.layout.view_login, this)

        et_username.addTextChangedListener(userNameWatcher)
        et_password.addTextChangedListener(passwordWatcher)
        et_code.addTextChangedListener(codeWatcher)
        et_question.addTextChangedListener(questionWatcher)

        val spinnerData = resources.getStringArray(R.array.login_item_spinner)
        val spinnerAdapter = ArrayAdapter<String>(context, R.layout.login_spinner, spinnerData)
        spinner.adapter = spinnerAdapter

        
//        Glide.with(context).load(Constant.BASE_URL + Constant.SAFE_CODE)
//            .diskCacheStrategy(DiskCacheStrategy.NONE)
//            .skipMemoryCache(true)
//            .centerCrop()
//            .into(btn_get_code)
    }

    @TextProp
    fun setUserName(username: CharSequence?) {
        et_username.setTextIfDifferent(username)
    }

    @TextProp
    fun setPassword(password: CharSequence?) {
        et_password.setTextIfDifferent(password)
    }

    @TextProp
    fun setCode(code: CharSequence?) {
        et_code.setTextIfDifferent(code)
    }

    @TextProp
    fun setQuestion(code: CharSequence?) {
        et_question.setTextIfDifferent(code)
    }

   @ModelProp(ModelProp.Option.IgnoreRequireHashCode)
   fun isShow(show : Boolean){
       if(show){
           et_question.visibility = View.VISIBLE
       }else{
           et_question.visibility = View.GONE
       }
   }


    @set:CallbackProp
    var onUserNameChanged: ((newText: String) -> Unit)? = null

    @set:CallbackProp
    var onPasswordChanged: ((newText: String) -> Unit)? = null

    @set:CallbackProp
    var onCodeChanged: ((newText: String) -> Unit)? = null

    @set:CallbackProp
    var onQuestionChanged: ((newText: String) -> Unit)? = null


    /**
     * 登录的点击事件
     */
    @ModelProp(ModelProp.Option.IgnoreRequireHashCode)
    fun setClickListener(listener: OnClickListener?) {
        iv_login.setOnClickListener(listener)
    }

    /**
     * 下拉框选择
     */
    @CallbackProp
    fun setSpinnerItemSelectedListener(listener: AdapterView.OnItemSelectedListener?) {
        spinner.onItemSelectedListener = listener
    }

    /**
     * 跳过登录
     */
    @CallbackProp
    fun setJumpClickListener(listener: OnClickListener?) {
        loginJump.setOnClickListener(listener)
    }

    /**
     * 验证码点击
     */
    @ModelProp(ModelProp.Option.IgnoreRequireHashCode)
    fun setCodeClickListener(listener: OnClickListener?) {
        btn_get_code.setOnClickListener(listener)
    }

    /**
     * 第一次获取验证码
     */
    @ModelProp(ModelProp.Option.IgnoreRequireHashCode)
    fun setimageBitmap(bitmap : Bitmap?){
        btn_get_code.setImageBitmap(bitmap)
    }

    /**
     * 立即注册
     */
    @CallbackProp
    fun setRegisterClickListener(listener: OnClickListener?) {
        register.setOnClickListener(listener)
    }

    /**
     * 找回密码
     */
    @CallbackProp
    fun setFindClickListener(listener: OnClickListener?) {
        findpassword.setOnClickListener(listener)
    }
}

class SimpleTextWatcher(val onTextChanged: (newText: String) -> Unit) : TextWatcher {
    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        onTextChanged(s.toString())
    }

}