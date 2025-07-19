package template.composemultiplatform.html

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.stack.webhistory.DefaultWebHistoryController
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import com.arkivanov.essenty.lifecycle.stop
import js.import.import
import kotlinx.browser.document
import kotlinx.browser.window
import org.jetbrains.compose.web.renderComposable
import org.kodein.di.DI
import org.w3c.dom.Document
import org.w3c.notifications.*
import template.composemultiplatform.html.root.RootContent
import template.composemultiplatform.shared.SharedRes
import template.composemultiplatform.shared.common.data.SqlDriverFactoryProvider
import template.composemultiplatform.shared.common.di.getMainDI
import template.composemultiplatform.shared.common.resources.Localizer
import template.composemultiplatform.shared.root.RootComponent

@OptIn(ExperimentalDecomposeApi::class)
suspend fun main() {
    val lifecycle = LifecycleRegistry()
    val strings = SharedRes.strings.stringsLoader.getOrLoad()

    // imports
    import<Any>("@fontsource/roboto")
    import<Any>("@material-symbols/font-400/outlined.css")

    // configure di
    val di = DI {
        val mainDi = getMainDI(
            driverFactory = SqlDriverFactoryProvider().getDriverFactory(),
            localizer = Localizer(strings)
        )
        import(mainDi)
    }

    // request permissions
    when (Notification.permission) {
        NotificationPermission.GRANTED -> {}
        NotificationPermission.DEFAULT -> {
            Notification.requestPermission().then {
                println("Granted permission")
            }
        }
        NotificationPermission.DENIED -> {}
    }

    val root = RootComponent(
        context = DefaultComponentContext(lifecycle = lifecycle),
        deepLink = RootComponent.DeepLink.Web(path = window.location.pathname),
        webHistoryController = DefaultWebHistoryController(),
        di = di
    )
    lifecycle.attachToDocument()

    renderComposable(rootElementId = "root") {
        RootContent(root, strings)
    }
}

private fun LifecycleRegistry.attachToDocument() {
    fun onVisibilityChanged() {
        if (document.visibilityState == "visible") {
            resume()
        } else {
            stop()
        }
    }

    onVisibilityChanged()

    document.addEventListener(type = "visibilitychange", callback = { onVisibilityChanged() })
}

private val Document.visibilityState: String get() = asDynamic().visibilityState.unsafeCast<String>()
