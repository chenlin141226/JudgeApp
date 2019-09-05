package com.judge.app.activities

import android.os.Bundle
import android.text.Layout
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.judge.R
import com.judge.app.core.BaseActivity
import com.vondear.rxtool.RxTextTool
import com.vondear.rxtool.view.RxToast
import kotlinx.android.synthetic.main.activity_login.*

/**
 * @author: jaffa
 * @date: 2019/7/28
 * 登录Activity
 */

class LoggingActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun initView(navController: NavController) {
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
               // RxToast.info(baseContext, "点击成功", Toast.LENGTH_SHORT, true).show()
                navController.navigateUp()
            }


            override fun updateDrawState(ds: TextPaint) {
                ds.color = ContextCompat.getColor(applicationContext,R.color.colorPrimary)
                ds.isUnderlineText = false
            }
        }

        // 响应点击事件的话必须设置以下属性
        tv_about_spannable.movementMethod = LinkMovementMethod.getInstance()

        RxTextTool.getBuilder("")
            .setBold()
            .setAlign(Layout.Alignment.ALIGN_CENTER)
            .append(resources.getString(R.string.register_bottom_message))
            .append(resources.getString(R.string.login))
            .setClickSpan(clickableSpan)
            .into(tv_about_spannable)
    }
}