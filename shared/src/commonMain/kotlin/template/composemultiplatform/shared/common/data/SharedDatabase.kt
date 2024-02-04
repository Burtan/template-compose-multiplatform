package template.composemultiplatform.shared.common.data

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import template.composemultiplatform.shared.ExampleDatabase

class SharedDatabase(private val driverFactory: DriverFactory) {

    private var database: ExampleDatabase? = null

    private suspend fun initDatabase(): ExampleDatabase {
        return Mutex().withLock {
            database ?: kotlin.run {
                val driver = driverFactory.createDriver()

                val newDb = ExampleDatabase(driver)
                database = newDb
                newDb
            }
        }
    }

    suspend operator fun <R> invoke(block: suspend ExampleDatabase.() -> R): R {
        val db = initDatabase()
        return block(db)
    }

}
