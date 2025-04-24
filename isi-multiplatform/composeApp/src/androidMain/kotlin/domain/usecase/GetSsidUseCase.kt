package domain.usecase

import android.content.Context
import android.net.wifi.WifiManager
import android.os.Build
import platform.AppContext

actual fun getSSID(context: AppContext): String? {
    val wifiManager = AppContext.get().applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    val wifiInfo = wifiManager.connectionInfo

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        wifiInfo.ssid.replace("\"", "")
    } else {
        wifiInfo.ssid.replace("\"", "")
    }
}