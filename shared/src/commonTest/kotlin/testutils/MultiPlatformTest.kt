package testutils

import template.composemultiplatform.shared.common.data.DriverFactory
import template.composemultiplatform.shared.common.resources.Localizer

expect abstract class MultiPlatformTest() {
    suspend fun getTestImageData(): ByteArray
    fun getTestSqlDriverFactory(): DriverFactory
    suspend fun getTestLocalizer(): Localizer
}
