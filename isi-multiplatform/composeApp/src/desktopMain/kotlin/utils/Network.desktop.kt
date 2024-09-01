package utils

actual fun getSSID(context: AppContext): String? {
    val process = Runtime.getRuntime().exec("iwgetid -r")
    return process.inputStream.bufferedReader().readText().trim().replace("\"", "")
}
