package testutils

import kotlinx.coroutines.test.runTest
import template.composemultiplatform.shared.SharedRes
import kotlin.test.Test

class GeneratedResourcesTest {

    /**
     * This tests generated resources from
     * shared module: year, version and licenses
     * web module: licenses
     */
    @Test
    fun readBuildInfoTest() = runTest {
        SharedRes.files.year_txt.readText()
    }

}
