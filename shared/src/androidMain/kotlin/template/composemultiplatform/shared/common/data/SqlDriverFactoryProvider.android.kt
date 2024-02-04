package template.composemultiplatform.shared.common.data

import android.content.Context
import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import template.composemultiplatform.shared.ExampleDatabase

actual class SqlDriverFactoryProvider(private val androidContext: Context) {
    actual fun getDriverFactory(name: String): DriverFactory {
        return object : DriverFactory {
            override suspend fun createDriver(): SqlDriver {
                return AndroidSqliteDriver(
                    schema = ExampleDatabase.Schema.synchronous(),
                    context = androidContext,
                    name = name,
                )
            }
            override fun isAsync() = false
            override fun isPersistent() = true
        }
    }
}
