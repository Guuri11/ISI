package data.sources

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.guuri11.isi.AppDatabase

actual class DatabaseDriverFactory : DatabaseDriverFactoryI {
    override fun createDriver(): SqlDriver {
        val driver: SqlDriver = JdbcSqliteDriver(url = "jdbc:sqlite:isidb")
        AppDatabase.Schema.create(driver)
        return driver
    }
}