package utils

import getPlatform

expect fun getSSID(context: AppContext): String?

val wifiSSIDs = wifis.split("++")
val isLocal = wifiSSIDs.all { ssid ->
    ssid != getSSID(context = AppContext)
}