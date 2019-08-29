package com.judge.views

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.judge.R
import com.vondear.rxtool.view.RxToast
import com.vondear.rxui.view.dialog.RxDialog
import com.vondear.rxui.view.dialog.RxDialogLoading
import com.vondear.rxui.view.progressing.SpinKitView

class DialogLoading : RxDialog {

    var loadingView: SpinKitView? = null
        private set
    var dialogContentView: View? = null
        private set
    var textView: TextView? = null
        private set

    constructor(context: Context, themeResId: Int) : super(context, themeResId) {
        initView(context)
    }

    constructor(
        context: Context,
        cancelable: Boolean,
        cancelListener: DialogInterface.OnCancelListener?
    ) : super(context, cancelable, cancelListener) {
        initView(context)
    }

    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Activity) : super(context) {
        initView(context)
    }

    constructor(context: Context, alpha: Float, gravity: Int) : super(context, alpha, gravity) {
        initView(context)
    }

    private fun initView(context: Context) {
        dialogContentView =
            LayoutInflater.from(context).inflate(R.layout.loading_dialog, null)
        loadingView = dialogContentView!!.findViewById(R.id.spin_kit)
        textView = dialogContentView!!.findViewById(R.id.name)
        setContentView(dialogContentView!!)
    }

    fun setLoadingText(charSequence: CharSequence) {
        textView!!.text = charSequence
    }

    fun cancel(code: RxDialogLoading.RxCancelType, str: String) {
        cancel()
        when (code) {
            RxDialogLoading.RxCancelType.normal -> RxToast.normal(str)
            RxDialogLoading.RxCancelType.error -> RxToast.error(str)
            RxDialogLoading.RxCancelType.success -> RxToast.success(str)
            RxDialogLoading.RxCancelType.info -> RxToast.info(str)
            else -> RxToast.normal(str)
        }
    }

    fun cancel(str: String) {
        cancel()
        RxToast.normal(str)
    }
}
