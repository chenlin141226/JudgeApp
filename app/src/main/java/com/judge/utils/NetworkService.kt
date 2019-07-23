package com.judge.utils

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.IBinder

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
                networkBroadCast(context, intent, -1)
                return
            }
            val type = info.type
            when (type) {
                ConnectivityManager.TYPE_WIFI -> networkBroadCast(context, intent, 1)
                ConnectivityManager.TYPE_MOBILE -> networkBroadCast(context, intent, 2)
                else -> {
                }
            }
        }
    }

    private fun networkBroadCast(context: Context, intent: Intent, netState: Int) {
        intent.action = NetworkUtils.NET_BROADCAST_ACTION
        intent.putExtra(NetworkUtils.NET_STATE_NAME, netState)
        context.sendBroadcast(intent)
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

    companion object {

        private val GRAY_SERVICE_ID = 1001
    }

}
