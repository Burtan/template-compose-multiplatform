package template.composemultiplatform.shared.common.di

import io.ktor.client.engine.*
import io.ktor.client.engine.js.*

actual fun getHttpClientEngine(): HttpClientEngine {
    return Js.create()
}