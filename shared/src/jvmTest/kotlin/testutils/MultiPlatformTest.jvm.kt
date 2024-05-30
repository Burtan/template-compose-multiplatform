package testutils

import io.ktor.util.*
import template.composemultiplatform.shared.MR
import template.composemultiplatform.shared.common.data.DriverFactory
import template.composemultiplatform.shared.common.data.SqlDriverFactoryProvider
import template.composemultiplatform.shared.common.resources.Localizer
import kotlin.random.Random

actual abstract class MultiPlatformTest actual constructor() {
    actual suspend fun getTestImageData() = MR.files.test_image_txt.readText().decodeBase64Bytes()
    actual fun getTestSqlDriverFactory(): DriverFactory {
        return SqlDriverFactoryProvider().getDriverFactory(Random.nextLong().toString())
    }
    actual suspend fun getTestLocalizer() = Localizer()
}
