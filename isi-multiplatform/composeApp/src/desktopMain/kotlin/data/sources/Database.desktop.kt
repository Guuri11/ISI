package data.sources

import com.guuri11.isi.AppDatabase
import com.guuri11.isi.Settings
import domain.entity.GptSetting

actual class Database {
    val database = AppDatabase(DatabaseDriverFactory().createDriver())
    private val dbQuery = database.appDatabaseQueries
    actual fun getSettings(): Settings {
        return try {
            dbQuery.getSettings().executeAsOne()
        } catch (ex: NullPointerException) {
            dbQuery.insertSetting(
                id = 1,
                modelAI = GptSetting.GPT_4O_MINI.value,
                modelAIApiKey = "",
                wifis = null,
                server = "http://192.168.1.76:8080"
            )
            getSettings()
        }
    }

    actual fun modifySettings(settings: Settings) {
        dbQuery.transaction {
            dbQuery.removeAllSettings()
            dbQuery.insertSetting(
                id = 1,
                modelAI = settings.modelAI,
                modelAIApiKey = settings.modelAIApiKey,
                wifis = settings.wifis,
                server = settings.server
            )
        }
    }
}