package template.composemultiplatform.shared.common.di

import io.ktor.client.engine.*
import io.ktor.client.engine.java.*

actual fun getHttpClientEngine(): HttpClientEngine {
    return Java.create()
}
