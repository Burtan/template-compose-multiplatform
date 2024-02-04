package template.composemultiplatform.shared.common.data

import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import template.composemultiplatform.shared.ExampleDatabase

actual class SqlDriverFactoryProvider {
    actual fun getDriverFactory(name: String): DriverFactory {
        return object : DriverFactory {
            override suspend fun createDriver(): SqlDriver {
                return NativeSqliteDriver(ExampleDatabase.Schema.synchronous(), name)
            }
            override fun isAsync() = false
            override fun isPersistent() = true
        }
    }
}
