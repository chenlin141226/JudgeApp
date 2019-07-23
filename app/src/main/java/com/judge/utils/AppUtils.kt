package com.judge.utils

import android.content.Context
import android.content.res.AssetManager
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import androidx.annotation.ArrayRes
import androidx.annotation.StringRes

import java.util.Timer
import java.util.TimerTask

/**
 * APP工具类
 * Created by zzq on 2016/12/17.
 */
object AppUtils {

    var appContext: Context? = null
        private set
    private var mUiThread: Thread? = null
    private var mTimer: Timer? = null

    private val sHandler = Handler(Looper.getMainLooper())

    val assets: AssetManager
        get() = appContext!!.assets
    val resource: Resources
        get() = appContext!!.resources

    val isUIThread: Boolean
        get() = Thread.currentThread() === mUiThread

    fun init(context: Context) { //在Application中初始化
        appContext = context
        mUiThread = Thread.currentThread()
        mTimer = Timer()
    }

    fun getDimension(id: Int): Float {
        return resource.getDimension(id)
    }

    fun getDrawable(resId: Int): Drawable {
        return appContext!!.resources.getDrawable(resId)
    }

    fun getColor(resId: Int): Int {
        return appContext!!.resources.getColor(resId)
    }

    fun getString(@StringRes resId: Int): String {
        return appContext!!.resources.getString(resId)
    }

    fun getStringArray(@ArrayRes resId: Int): Array<String> {
        return appContext!!.resources.getStringArray(resId)
    }

    fun runOnUI(r: Runnable) {
        sHandler.post(r)
    }

    fun runOnUIDelayed(r: Runnable, delayMills: Long) {
        sHandler.postDelayed(r, delayMills)
    }

    fun runOnUITask(r: TimerTask, delay: Long, rate: Long) {
        mTimer!!.schedule(r, delay, rate)
    }

    fun runCancel() {
        mTimer!!.cancel()
    }

    fun removeRunnable(r: Runnable?) {
        if (r == null) {
            sHandler.removeCallbacksAndMessages(null)
        } else {
            sHandler.removeCallbacks(r)
        }
    }
}
