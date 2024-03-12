package template.composemultiplatform.shared.test

import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class JvmTest {

    @Test
    fun wontRun() = runTest {
        println("Test")
    }
}
