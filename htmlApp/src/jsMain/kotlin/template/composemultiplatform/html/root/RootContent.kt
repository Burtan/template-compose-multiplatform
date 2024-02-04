package template.composemultiplatform.html.root

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import dev.icerock.moko.resources.provider.JsStringProvider
import template.composemultiplatform.html.subscribeAsState
import template.composemultiplatform.shared.root.RootComponent
import template.composemultiplatform.shared.root.RootInterface

@Composable
fun RootContent(rootCmp: RootComponent, strings: JsStringProvider) {
    val childStack by rootCmp.childStack.subscribeAsState()

    when (val child = childStack.active.instance) {
        is RootInterface.Child.BirdsChild -> TODO()
        is RootInterface.Child.FishesChild -> TODO()
        is RootInterface.Child.MammalsChild -> TODO()
    }
}
