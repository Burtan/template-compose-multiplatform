package template.composemultiplatform.html.test

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import template.composemultiplatform.html.test.common.findChild
import template.composemultiplatform.html.test.common.setIndeterminateProgressToZero
import dev.icerock.moko.resources.provider.JsStringProvider
import js.import.import
import js.promise.Promise
import js.promise.await
import kotlinx.browser.document
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.takeWhile
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.maxHeight
import org.jetbrains.compose.web.css.maxWidth
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.testutils.ComposeWebExperimentalTestsApi
import org.jetbrains.compose.web.testutils.runTest
import template.composemultiplatform.shared.MR
import template.composemultiplatform.shared.common.Testable
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
    import<Any>("@fontsource/roboto").await()
    import<Any>("@material-symbols/font-400/outlined.css").await()
    val crypto: dynamic = import<Any>("crypto-es/lib/md5.js").await()
    val htmlToImage: dynamic = import<Any>("html-to-image").await()
    val chaiKarma: dynamic = import<Any>("chai-karma-snapshot").await()
    val chai: dynamic = import<Any>("chai").await()
    val describe: (title: String, fn: (suite: dynamic) -> dynamic) -> dynamic = js("describe")
    val it: (title: String, fn: () -> dynamic) -> dynamic = js("it")

    val strings = MR.strings.stringsLoader.getOrLoad()
    val finishedFlow = MutableStateFlow(false)
    val pngs = mutableMapOf<String, String>()

    val viewport = js("viewport")
    viewport.set(viewportWidth, viewportHeight)

    chai.use(chaiKarma.matchSnapshot)

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

                delay(500)

                val imgPromise: Promise<String> = htmlToImage.toPng(domElement)
                val imgBase64 = imgPromise.await()
                pngs[it.testTitle] = imgBase64
            }

            // save doms after composition has finished
            // dont save them during composition as creating the tests disturbs composition
            describe("${T::class.simpleName}") { _ ->
                pngs.forEach { entry ->
                    it(entry.key) {
                        val hash = crypto.MD5(entry.value).toString()
                        println("\nRendered snapshot ${entry.key} to base64 with ${entry.value.length} chars \n")
                        println("\n$hash\n")

                        // only for debugging! Prints large chars and makes testing buggy
                        // println("\n${entry.value}\n")

                        if (useHashes) {
                            chai.assert.matchSnapshot(hash)
                        } else {
                            chai.assert.matchSnapshot(entry.value)
                        }
                    }
                }
            }

            finishedFlow.value = true
        }

        Div(
            attrs = {
                id("root")
                style {
                    // adjust for border
                    height((viewportHeight - 2).px)
                    width((viewportWidth - 2).px)
                    maxHeight((viewportHeight - 2).px)
                    maxWidth((viewportWidth - 2).px)
                }
            }
        ) {
            preview?.let {
                content(it, strings)
            } ?: kotlin.run {
                Text("Initing ui")
            }
        }
    }

    finishedFlow
        .takeWhile { !it }
        .collect()

    delay(1000)
}
