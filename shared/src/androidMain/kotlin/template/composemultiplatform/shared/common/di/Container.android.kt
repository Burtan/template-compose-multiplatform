package template.composemultiplatform.shared.common.di

import io.ktor.client.engine.*
import io.ktor.client.engine.android.*

actual fun getHttpClientEngine(): HttpClientEngine {
    return Android.create()
}
