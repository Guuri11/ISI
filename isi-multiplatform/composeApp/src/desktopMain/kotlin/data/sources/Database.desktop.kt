package data.sources

import com.guuri11.isi.Settings

actual class Database {
    actual fun getSettings(): Settings {
        throw Exception("Not available in desktop yet")
    }

    actual fun modifySettings(settings: Settings) {
        throw Exception("Not available in desktop yet")
    }
}