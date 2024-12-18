package testutils

import io.ktor.util.*
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import template.composemultiplatform.shared.SharedRes
import template.composemultiplatform.shared.common.data.DriverFactory
import template.composemultiplatform.shared.common.data.SqlDriverFactoryProvider
import template.composemultiplatform.shared.common.resources.Localizer
import kotlin.random.Random

@RunWith(RobolectricTestRunner::class)
actual abstract class MultiPlatformTest {
    private val context = RuntimeEnvironment.getApplication()
    actual suspend fun getTestImageData() = SharedRes.files.test_image_txt.readText(context).decodeBase64Bytes()
    actual fun getTestSqlDriverFactory(): DriverFactory {
        return SqlDriverFactoryProvider(context).getDriverFactory(Random.nextLong().toString())
    }
    actual suspend fun getTestLocalizer() = Localizer(context)
}
