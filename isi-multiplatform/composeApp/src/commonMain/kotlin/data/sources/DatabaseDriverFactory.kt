package data.sources

import app.cash.sqldelight.db.SqlDriver

interface DatabaseDriverFactoryI {
    fun createDriver(): SqlDriver
}

expect class DatabaseDriverFactory : DatabaseDriverFactoryI {}
