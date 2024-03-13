package template.composemultiplatform.shared.common.data

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import template.composemultiplatform.shared.ExampleDatabase

actual class SqlDriverFactoryProvider {
    actual fun getDriverFactory(name: String): DriverFactory {
        return object : DriverFactory {
            override suspend fun createDriver(): SqlDriver {
                return JdbcSqliteDriver("jdbc:sqlite:$name")
                    .apply { ExampleDatabase.Schema.create(this) }
            }
            override fun isAsync() = false
            override fun isPersistent() = true
        }
    }
}
