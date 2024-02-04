package template.composemultiplatform.shared.common.di

import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import template.composemultiplatform.shared.common.data.DaoFacadeImpl
import template.composemultiplatform.shared.common.data.DriverFactory
import template.composemultiplatform.shared.common.resources.Localizer

fun getMainDI(
    driverFactory: DriverFactory,
    localizer: Localizer,
    engine: HttpClientEngine? = null
) = DI.Module("main") {
    val appScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    val httpClient = HttpClient(engine ?: getHttpClientEngine()) {
        install(ContentNegotiation) {
            json()
        }
    }
    val dao = DaoFacadeImpl(driverFactory)

    bindSingleton { localizer }
    bindSingleton { appScope }
    bindSingleton { dao }
    bindSingleton { httpClient }
}

expect fun getHttpClientEngine(): HttpClientEngine
