package com.judge.app.core

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.airbnb.mvrx.BaseMvRxActivity
import com.vondear.rxtool.RxPermissionsTool

abstract class BaseActivity : BaseMvRxActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBar()
        RxPermissionsTool.with(this).addPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            .addPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
            .addPermission(Manifest.permission.READ_EXTERNAL_STORAGE).addPermission(Manifest.permission.CAMERA)
            .addPermission(Manifest.permission.READ_PHONE_STATE)
            .initPermission()
    }

    private fun setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.statusBarColor = 0
        }
    }
}