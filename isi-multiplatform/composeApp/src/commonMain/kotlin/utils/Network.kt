package utils

expect fun getSSID(context: AppContext): String?


fun isLocal(wifis: String): Boolean {
    val wifiSSIDs = wifis.split("++")

    return isAllSSIDsNotConnected(wifiSSIDs)
}


fun isAllSSIDsNotConnected(wifiSSIDs: List<String>): Boolean {
    return wifiSSIDs.all { ssid ->
        ssid != getSSID(context = AppContext)
    }
}
