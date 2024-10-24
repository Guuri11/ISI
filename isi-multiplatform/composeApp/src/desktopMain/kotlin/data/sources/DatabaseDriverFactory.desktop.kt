package data.sources

import app.cash.sqldelight.db.SqlDriver

actual class DatabaseDriverFactory : DatabaseDriverFactoryI {
    override fun createDriver(): SqlDriver {
        throw Exception("Not available for Desktop for now")
    }
}