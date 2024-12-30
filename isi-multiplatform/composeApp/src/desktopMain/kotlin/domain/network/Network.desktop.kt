package domain.network

import platform.AppContext

actual fun getSSID(context: AppContext): String? {
    val os = System.getProperty("os.name").lowercase()
    if (os.contains("win")) {
        val process = Runtime.getRuntime().exec("netsh wlan show interfaces")
        val result = process.inputStream.bufferedReader().readText()

        val ssidLine = result.lines().firstOrNull { it.trim().startsWith("SSID") && !it.contains("BSSID") }

        return ssidLine?.substringAfter(":")?.trim()
    } else {
        val process = Runtime.getRuntime().exec("iwgetid -r")
        return process.inputStream.bufferedReader().readText().trim().replace("\"", "")
    }
}
