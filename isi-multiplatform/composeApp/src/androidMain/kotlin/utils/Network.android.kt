package utils

import android.content.Context
import android.net.wifi.WifiManager
import android.os.Build

actual fun getSSID(context: Any?): String? {
    val androidContext = context as? Context ?: return null
    val wifiManager = androidContext.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    val wifiInfo = wifiManager.connectionInfo

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        wifiInfo.ssid
    } else {
        wifiInfo.ssid
    }
}