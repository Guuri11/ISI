package utils

actual fun getSSID(context: Any?): String? {
    val process = Runtime.getRuntime().exec("iwgetid -r")
    return process.inputStream.bufferedReader().readText().trim()
}
