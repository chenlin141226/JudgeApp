package com.judge.utils

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.IBinder
import com.vondear.rxtool.view.RxToast

/**
 * 网络状态监听服务
 *
 *
 * Created by zzq on 2016/11/17.
 */
class NetworkService : Service() {

    private val mReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val info = connectivityManager.activeNetworkInfo
            if (info == null) {
                RxToast.showToast("当前没有网络")
                NetworkUtils.setOnChangeInternet(false)//设置网络监听
                LogUtils.i(NetworkUtils.TAG, "网络更改为 无网络  CURRENT_NETWORK_STATE =${NetworkUtils.CURRENT_NETWORK_STATE}")
                return
            }
            when (info.type) {
                1 -> {

                    LogUtils.i(
                        NetworkUtils.TAG,
                        "网络更改为 WIFI网络  CURRENT_NETWORK_STATE=${NetworkUtils.CURRENT_NETWORK_STATE}"
                    )
                }
                2 -> {
                    LogUtils.i(
                        NetworkUtils.TAG,
                        "网络更改为 移动网络  CURRENT_NETWORK_STATE =${NetworkUtils.CURRENT_NETWORK_STATE}"
                    )
                }
                else -> {
                    LogUtils.i(
                        NetworkUtils.TAG,
                        "网络更改为 移动网络  CURRENT_NETWORK_STATE =${NetworkUtils.CURRENT_NETWORK_STATE}"
                    )
                }
            }
            NetworkUtils.setOnChangeInternet(true)//设置网络监听
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        val filter = IntentFilter()
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(mReceiver, filter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(mReceiver)
    }

}
