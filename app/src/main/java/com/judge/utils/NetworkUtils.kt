package com.judge.utils

import android.app.Activity
import android.app.PendingIntent
import android.content.*
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.IBinder
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Log

import java.lang.reflect.Method

/**
 * Created by zzq on 2016/11/17.
 */
object NetworkUtils {
    private val TAG = NetworkUtils::class.java.simpleName

    /**
     * 接受网络状态的广播Action
     */
    val NET_BROADCAST_ACTION = "com.network.state.action"
    val NET_STATE_NAME = "network_state"
    /**
     * 实时更新网络状态<br></br>
     * -1为网络无连接<br></br>
     * 1为WIFI<br></br>
     * 2为移动网络<br></br>
     */
    var CURRENT_NETWORK_STATE = -1

    /**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     *
     * @return true 表示开启
     */
    // GPS
    // WLAN或移动网络(3G/2G)
    val isGpsEnabled: Boolean
        get() {
            val locationManager = AppUtils.appContext!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            if (gps || network) {
                Log.i("demo", "GPS Ensable")
                return true
            }
            return false
        }

    /**
     * 接受服务上发过来的广播
     */
    private val mReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent?) {
            if (intent != null) {
                CURRENT_NETWORK_STATE = intent.extras!!.get(NET_STATE_NAME) as Int
                when (CURRENT_NETWORK_STATE) {
                    -1 -> {
                        ToastUtils.showSingleLongToast("当前没有网络")
                        setOnChangeInternet(false)//设置网络监听
                        LogUtils.i(TAG, "网络更改为 无网络  CURRENT_NETWORK_STATE =$CURRENT_NETWORK_STATE")
                    }
                    1 -> {
                        setOnChangeInternet(true)//设置网络监听
                        LogUtils.i(TAG, "网络更改为 WIFI网络  CURRENT_NETWORK_STATE=$CURRENT_NETWORK_STATE")
                    }
                    2 -> {
                        setOnChangeInternet(true)//设置网络监听
                        LogUtils.i(TAG, "网络更改为 移动网络  CURRENT_NETWORK_STATE =$CURRENT_NETWORK_STATE")
                    }
                    else -> {
                    }
                }
            }
        }
    }

    //设置网络改变监听
    private var mListener: OnChangeInternetListener? = null

    enum class NetType private constructor(var value: Int, var desc: String) {
        None(1, "无网络连接"),
        Mobile(2, "蜂窝移动网络"),
        Wifi(4, "Wifi网络"),
        Other(8, "未知网络")
    }

    enum class NetWorkType private constructor(var value: Int, var desc: String) {
        UnKnown(-1, "未知网络"),
        Wifi(1, "Wifi网络"),
        Net2G(2, "2G网络"),
        Net3G(3, "3G网络"),
        Net4G(4, "4G网络")
    }

    /**
     * 获取ConnectivityManager
     */
    fun getConnectivityManager(context: Context): ConnectivityManager {
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    /**
     * 获取TelephonyManager
     */
    fun getTelephonyManager(context: Context): TelephonyManager {
        return context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    }

    /**
     * 判断网络是否连接(包含MobileNet，Wifi)
     *
     * @return
     */
    fun isConnected(context: Context): Boolean {
        val net = getConnectivityManager(context).activeNetworkInfo
        return net != null && net.isConnected
    }

    /**
     * 判断有无网络正在连接中或已连接（查找网络、校验、获取IP等）(包含MobileNet，Wifi)
     *
     * @return
     */
    fun isConnectedOrConnecting(context: Context): Boolean {
        val nets = getConnectivityManager(context).allNetworkInfo
        if (nets != null) {
            for (net in nets) {
                if (net.isConnectedOrConnecting) {
                    return true
                }
            }
        }
        return false
    }

    /**
     * 获取当前网络类型(Wifi或移动网络)
     *
     * @param context
     * @return
     */
    fun getConnectedType(context: Context): NetType {
        val net = getConnectivityManager(context).activeNetworkInfo
        if (net != null) {
            when (net.type) {
                ConnectivityManager.TYPE_WIFI -> return NetType.Wifi
                ConnectivityManager.TYPE_MOBILE -> return NetType.Mobile
                else -> return NetType.Other
            }
        }
        return NetType.None
    }

    /**
     * 是否存在有效的WIFI连接
     *
     * @param context
     * @return
     */
    fun isWifiConnected(context: Context): Boolean {
        val net = getConnectivityManager(context).activeNetworkInfo
        return net != null && net.type == ConnectivityManager.TYPE_WIFI && net.isConnected
    }

    /**
     * 是否存在有效的移动连接
     *
     * @param context
     * @return
     */
    fun isMobileConnected(context: Context): Boolean {
        val net = getConnectivityManager(context).activeNetworkInfo
        return net != null && net.type == ConnectivityManager.TYPE_MOBILE && net.isConnected
    }

    /**
     * 检测当前网络是否可用
     *
     * @param context
     * @return
     */
    fun isAvailable(context: Context): Boolean {
        return isWifiAvailable(context) || isMobileAvailable(context) && isMobileEnabled(context)
    }

    /**
     * 判断是否有可用状态的Wifi，以下情况返回false：
     * 1. 设备wifi开关关掉;
     * 2. 已经打开飞行模式；
     * 3. 设备所在区域没有信号覆盖；
     * 4. 设备在漫游区域，且关闭了网络漫游。
     *
     * @param context
     * @return boolean wifi为可用状态（不一定成功连接，即Connected）即返回ture
     */
    fun isWifiAvailable(context: Context): Boolean {
        val nets = getConnectivityManager(context).allNetworkInfo
        if (nets != null) {
            for (net in nets) {
                if (net.type == ConnectivityManager.TYPE_WIFI) {
                    return net.isAvailable
                }
            }
        }
        return false
    }

    /**
     * 判断有无可用状态的移动网络，注意关掉设备移动网络直接不影响此函数。
     * 也就是即使关掉移动网络，那么移动网络也可能是可用的(彩信等服务)，即返回true。
     * 以下情况它是不可用的，将返回false：
     * 1. 设备打开飞行模式；
     * 2. 设备所在区域没有信号覆盖；
     * 3. 设备在漫游区域，且关闭了网络漫游。
     *
     * @param context
     * @return boolean
     */
    fun isMobileAvailable(context: Context): Boolean {
        val nets = getConnectivityManager(context).allNetworkInfo
        if (nets != null) {
            for (net in nets) {
                if (net.type == ConnectivityManager.TYPE_MOBILE) {
                    return net.isAvailable
                }
            }
        }
        return false
    }

    /**
     * 设备是否打开蜂窝移动网络开关
     *
     * @param context
     * @return
     */
    fun isMobileEnabled(context: Context): Boolean {
        try {
            val getMobileDataEnabledMethod = ConnectivityManager::class.java.getDeclaredMethod("getMobileDataEnabled")
            getMobileDataEnabledMethod.isAccessible = true
            return getMobileDataEnabledMethod.invoke(getConnectivityManager(context)) as Boolean
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return true// 反射失败，默认开启
    }

    /**
     * 打印当前各种网络状态
     *
     * @return boolean
     */
    fun printNetworkInfo(context: Context): Boolean {
        val connectivity = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivity != null) {
            val info = connectivity.allNetworkInfo
            if (info != null) {
                for (i in info.indices) {
                    LogUtils.i(TAG, "NetworkInfo[" + i + "]isAvailable : " + info[i].isAvailable)
                    LogUtils.i(TAG, "NetworkInfo[" + i + "]isConnected : " + info[i].isConnected)
                    LogUtils.i(
                        TAG,
                        "NetworkInfo[" + i + "]isConnectedOrConnecting : " + info[i].isConnectedOrConnecting
                    )
                    LogUtils.i(TAG, "NetworkInfo[" + i + "]: " + info[i])
                }
                LogUtils.i(TAG, "\n")
            } else {
                LogUtils.i(TAG, "getAllNetworkInfo is null")
            }
        }
        return false
    }

    /**
     * get connected network type by [ConnectivityManager]
     *
     *
     * such as WIFI, MOBILE, ETHERNET, BLUETOOTH, etc.
     *
     * @return [ConnectivityManager.TYPE_WIFI], [ConnectivityManager.TYPE_MOBILE],
     * [ConnectivityManager.TYPE_ETHERNET]...
     */
    fun getConnectedTypeINT(context: Context): Int {
        val net = getConnectivityManager(context).activeNetworkInfo
        return net?.type ?: -1
    }

    /**
     * 获取网络连接类型
     *
     *
     * GPRS    2G(2.5) General Packet Radia Service 114kbps
     * EDGE    2G(2.75G) Enhanced Data Rate for GSM Evolution 384kbps
     * UMTS    3G WCDMA 联通3G Universal Mobile Telecommunication System 完整的3G移动通信技术标准
     * CDMA    2G 电信 Code Division Multiple Access 码分多址
     * EVDO_0  3G (EVDO 全程 CDMA2000 1xEV-DO) Evolution - Data Only (Data Optimized) 153.6kps - 2.4mbps 属于3G
     * EVDO_A  3G 1.8mbps - 3.1mbps 属于3G过渡，3.5G
     * 1xRTT   2G CDMA2000 1xRTT (RTT - 无线电传输技术) 144kbps 2G的过渡,
     * HSDPA   3.5G 高速下行分组接入 3.5G WCDMA High Speed Downlink Packet Access 14.4mbps
     * HSUPA   3.5G High Speed Uplink Packet Access 高速上行链路分组接入 1.4 - 5.8 mbps
     * HSPA    3G (分HSDPA,HSUPA) High Speed Packet Access
     * IDEN    2G Integrated Dispatch Enhanced Networks 集成数字增强型网络 （属于2G，来自维基百科）
     * EVDO_B  3G EV-DO Rev.B 14.7Mbps 下行 3.5G
     * LTE     4G Long Term Evolution FDD-LTE 和 TDD-LTE , 3G过渡，升级版 LTE Advanced 才是4G
     * EHRPD   3G CDMA2000向LTE 4G的中间产物 Evolved High Rate Packet Data HRPD的升级
     * HSPAP   3G HSPAP 比 HSDPA 快些
     *
     * @return [NetWorkType]
     */
    fun getNetworkType(context: Context): NetWorkType {
        val type = getConnectedTypeINT(context)
        when (type) {
            ConnectivityManager.TYPE_WIFI -> return NetWorkType.Wifi
            ConnectivityManager.TYPE_MOBILE, ConnectivityManager.TYPE_MOBILE_DUN, ConnectivityManager.TYPE_MOBILE_HIPRI, ConnectivityManager.TYPE_MOBILE_MMS, ConnectivityManager.TYPE_MOBILE_SUPL -> {
                val teleType = getTelephonyManager(context).networkType
                when (teleType) {
                    TelephonyManager.NETWORK_TYPE_GPRS, TelephonyManager.NETWORK_TYPE_EDGE, TelephonyManager.NETWORK_TYPE_CDMA, TelephonyManager.NETWORK_TYPE_1xRTT, TelephonyManager.NETWORK_TYPE_IDEN -> return NetWorkType.Net2G
                    TelephonyManager.NETWORK_TYPE_UMTS, TelephonyManager.NETWORK_TYPE_EVDO_0, TelephonyManager.NETWORK_TYPE_EVDO_A, TelephonyManager.NETWORK_TYPE_HSDPA, TelephonyManager.NETWORK_TYPE_HSUPA, TelephonyManager.NETWORK_TYPE_HSPA, TelephonyManager.NETWORK_TYPE_EVDO_B, TelephonyManager.NETWORK_TYPE_EHRPD, TelephonyManager.NETWORK_TYPE_HSPAP -> return NetWorkType.Net3G
                    TelephonyManager.NETWORK_TYPE_LTE -> return NetWorkType.Net4G
                    else -> return NetWorkType.UnKnown
                }
            }
            else -> return NetWorkType.UnKnown
        }
    }

    /**
     * 强制帮用户打开GPS
     */
    fun openGPS() {
        val GPSIntent = Intent()
        GPSIntent.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider")
        GPSIntent.addCategory("android.intent.category.ALTERNATIVE")
        GPSIntent.data = Uri.parse("custom:3")
        try {
            PendingIntent.getBroadcast(AppUtils.appContext, 0, GPSIntent, 0).send()
        } catch (e: PendingIntent.CanceledException) {
            e.printStackTrace()
        }

    }

    /**
     * 打开网络设置界面
     */
    fun openSetting(activity: Activity) {
        var intent: Intent? = null
        //判断手机系统的版本  即API大于10 就是3.0或以上版本
        if (10 < android.os.Build.VERSION.SDK_INT) {
            intent = Intent(Settings.ACTION_SETTINGS)
        } else {
            intent = Intent()
            val component = ComponentName("com.android.settings", "com.android.settings")
            intent.component = component
            intent.action = "android.intent.action.VIEW"
        }
        activity.startActivity(intent)
    }

    /**
     * 开启服务,实时监听网络变化（需要在清单文件配置Service）
     *
     * @param context
     */
    fun startNetService(context: Context) {
        //注册广播
        val intentFilter = IntentFilter()
        intentFilter.addAction(NET_BROADCAST_ACTION)
        context.registerReceiver(mReceiver, intentFilter)
        //开启服务
        val intent = Intent(context, NetworkService::class.java)
        context.bindService(intent, object : ServiceConnection {
            override fun onServiceDisconnected(name: ComponentName) {}

            override fun onServiceConnected(name: ComponentName, service: IBinder) {}
        }, Context.BIND_AUTO_CREATE)
    }

    interface OnChangeInternetListener {
        fun changeInternet(flag: Boolean)
    }

    fun setOnChangeInternetListener(listener: OnChangeInternetListener) {
        mListener = listener
    }

    private fun setOnChangeInternet(flag: Boolean) {
        if (mListener != null) {
            mListener!!.changeInternet(flag)
        }
    }
}
