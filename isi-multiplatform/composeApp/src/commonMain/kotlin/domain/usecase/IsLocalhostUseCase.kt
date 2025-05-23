package domain.usecase

import platform.AppContext

fun isLocalhostUseCase(wifis: String): Boolean {
    val wifiSSIDs = wifis.split("++")

    return isAllSSIDsNotConnected(wifiSSIDs)
}


private fun isAllSSIDsNotConnected(wifiSSIDs: List<String>): Boolean {
    return wifiSSIDs.all { ssid ->
        ssid != getSsidUseCase(context = AppContext)
    }
}
