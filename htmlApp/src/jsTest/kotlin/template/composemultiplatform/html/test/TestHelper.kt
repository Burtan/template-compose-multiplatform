package template.composemultiplatform.html.test

import androidx.compose.runtime.*

import dev.icerock.moko.resources.provider.JsStringProvider
import js.import.importAsync
import js.promise.Promise
import js.promise.await
import kotlinx.browser.document
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.takeWhile
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.testutils.ComposeWebExperimentalTestsApi
import org.jetbrains.compose.web.testutils.runTest
import org.w3c.dom.HTMLElement
import template.composemultiplatform.shared.SharedRes
import template.composemultiplatform.shared.common.Testable
import kotlin.test.assertTrue
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

@Suppress("UnsafeCastFromDynamic")
@OptIn(ComposeWebExperimentalTestsApi::class)
inline fun <reified T: Testable> runMobileTest(
    viewportWidth: Int = 375,
    viewportHeight: Int = 600,
    components: Array<T>,
    delay: Duration = 0.seconds,
    useHashes: Boolean = false,
    crossinline content: @Composable ((T, JsStringProvider) -> Unit)
) = runTest {
    // when executing multiple tests, old testContainers have to be removed
    document.getElementById("root")?.remove()

    // imports
    importAsync<Any>("@fontsource/roboto").await()
    importAsync<Any>("@material-symbols/font-400/outlined.css").await()
    val htmlToImage: dynamic = importAsync<Any>("html-to-image").await()
    val crypto: dynamic = importAsync<Any>("crypto-es/lib/md5.js").await()
    val snapshotState: SnapshotState = js("window.__snapshot__")

    val strings = SharedRes.strings.stringsLoader.getOrLoad()
    val finishedFlow = MutableStateFlow(false)
    val pngs = mutableMapOf<String, String>()

    val viewport = js("viewport")
    viewport.set(viewportWidth, viewportHeight)

    composition {
        var preview by remember { mutableStateOf<T?>(null) }

        LaunchedEffect(null) {
            // await initial composition
            delay(100.milliseconds)
            components.forEach {
                // assigning preview will trigger a recomposition

                // first reset the view, it is needed for triggered views
                // like snackbars and dialogs
                preview = null
                delay(100)
                preview = it

                delay(500)

                delay(delay)

                val domElement = document.body
                    ?.childNodes
                    ?.findChild("root") // get test container
                    ?.setIndeterminateProgressToZero()

                // clean focus
                (document.activeElement as? HTMLElement)?.blur()

                delay(500)

                val imgPromise: Promise<String> = htmlToImage.toPng(domElement)
                val imgBase64 = imgPromise.await()
                pngs[it.testTitle] = imgBase64
            }

            finishedFlow.value = true
        }

        // should match attributes of the real root element
        Div(
            attrs = {
                id("root")
                style {
                    // adjust for border
                    height((viewportHeight - 2).px)
                    width((viewportWidth - 2).px)
                    maxHeight((viewportHeight - 2).px)
                    maxWidth((viewportWidth - 2).px)

                    display(DisplayStyle.Flex)
                    flexDirection(FlexDirection.Column)
                    alignItems(AlignItems.Stretch)
                    justifyContent(JustifyContent.Center)
                }
            }
        ) {
            preview?.let {
                content(it, strings)
            } ?: run {
                Text("Initiating ui")
            }
        }
    }

    finishedFlow
        .takeWhile { !it }
        .collect()

    // save doms after composition has finished
    // don't save them during composition as creating the tests disturbs composition
    pngs.forEach { (key, value) ->
        val hash = crypto.MD5(value).toString()

        val snapshotPath = listOfNotNull(T::class.simpleName, key).toTypedArray()
        if (useHashes) {
            snapshotState.matchSnapshot(snapshotPath, 0, hash)
        } else {
            snapshotState.matchSnapshot(snapshotPath, 0, value)
        }
    }

    delay(1000)
}

fun SnapshotState.matchSnapshot(path: Array<String>, index: Int, received: String) {
    val snapshot = get(path, index)

    if (snapshot == null) {
        set(path, index, received)
    } else {
        val expected = snapshot.code
        val pass = match(received, expected)

        assertTrue(pass, "\n Snapshots differed: \nGot \n $received \nExpected \n $expected \n")
    }
}
