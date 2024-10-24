package data.sources

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.guuri11.isi.AppDatabase
import utils.AppContext

actual class DatabaseDriverFactory(private val context: AppContext) : DatabaseDriverFactoryI {
    override fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(AppDatabase.Schema, context.get(), "launch.db")
    }
}