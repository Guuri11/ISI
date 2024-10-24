package data.repository

import com.guuri11.isi.Settings
import data.sources.Database

// TODO: interface
class SettingsRepository(private val database: Database) {
    fun get(): Settings {
        return database.getSettings()
    }

    fun save(settings: Settings) {
        database.modifySettings(settings)
    }
}