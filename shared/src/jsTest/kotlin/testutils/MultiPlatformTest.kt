package testutils

import template.composemultiplatform.shared.common.data.DriverFactory
import template.composemultiplatform.shared.common.data.SqlDriverFactoryProvider
import template.composemultiplatform.shared.common.resources.Localizer
import io.ktor.util.*
import template.composemultiplatform.shared.MR
import kotlin.random.Random

actual abstract class MultiPlatformTest {
    actual suspend fun getTestImageData() = MR.files.test_image_txt.getText().decodeBase64Bytes()
    actual fun getTestSqlDriverFactory(): DriverFactory {
        return SqlDriverFactoryProvider().getDriverFactory(Random.nextLong().toString())
    }
    actual suspend fun getTestLocalizer() = Localizer(MR.strings.stringsLoader.getOrLoad())
}
