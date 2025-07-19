package template.composemultiplatform.html.test.snapshots

import template.composemultiplatform.html.root.RootContent
import template.composemultiplatform.html.test.runMobileTest
import template.composemultiplatform.shared.root.RootComponentPreview
import kotlin.test.Test

class RootTest {

    @Test
    fun snapshotRootMobile() = runMobileTest(
        components = RootComponentPreview.previews,
        //useHashes = true,
        //delay = 1.seconds
    ) { preview, strings ->
        RootContent(preview, strings)
    }

}
