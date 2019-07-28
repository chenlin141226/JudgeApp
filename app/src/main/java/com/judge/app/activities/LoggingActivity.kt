package com.judge.app.activities

import android.os.Bundle
import com.airbnb.mvrx.BaseMvRxActivity
import com.judge.R

/**
 * @author: jaffa
 * @date: 2019/7/28
 * 登录Activity
 */

class LoggingActivity : BaseMvRxActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }
}