package template.composemultiplatform.shared.common.data

import app.cash.sqldelight.async.coroutines.await
import app.cash.sqldelight.async.coroutines.awaitCreate
import app.cash.sqldelight.async.coroutines.awaitMigrate
import app.cash.sqldelight.async.coroutines.awaitQuery
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.worker.WebWorkerDriver
import org.w3c.dom.Worker
import template.composemultiplatform.shared.ExampleDatabase

@Suppress("UnsafeCastFromDynamic")
actual class SqlDriverFactoryProvider {
    actual fun getDriverFactory(name: String): DriverFactory {
        return object : DriverFactory {
            override suspend fun createDriver(): SqlDriver {
                val worker = Worker(js("""new URL("./sqlite-worker.js", import.meta.url)"""))
                    .apply {
                        postMessage(name)
                    }

                return WebWorkerDriver(worker = worker)
                    .apply {
                        migrateIfNeeded(this)
                    }
            }
            override fun isAsync() = true
            override fun isPersistent() = true
        }
    }
}

private suspend fun migrateIfNeeded(driver: SqlDriver) {
    val newVersion = ExampleDatabase.Schema.version
    val oldVersion = driver.awaitQuery(
        identifier = null,
        sql = "PRAGMA user_version",
        mapper = { cursor ->
            if (cursor.next().await()) {
                cursor.getLong(0)?.toInt()
            } else {
                null
            }
        },
        parameters = 0
    ) ?: 0

    if (oldVersion == 0) {
        ExampleDatabase.Schema.awaitCreate(driver)
        driver.await(null, "PRAGMA user_version = $newVersion", 0)
    } else if (oldVersion < newVersion) {
        ExampleDatabase.Schema.awaitMigrate(driver, oldVersion.toLong(), newVersion)
    }
}
