package template.composemultiplatform.html.root

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import dev.icerock.moko.resources.provider.JsStringProvider
import org.jetbrains.compose.web.dom.Text
import template.composemultiplatform.shared.root.RootInterface

@Composable
fun RootContent(rootCmp: RootInterface, strings: JsStringProvider) {
    val childStack by rootCmp.childStack.subscribeAsState()

    when (val child = childStack.active.instance) {
        is RootInterface.Child.BirdsChild -> {
            Text("Birds")
        }
        is RootInterface.Child.FishesChild -> {
            Text("Fishes")
        }
        is RootInterface.Child.MammalsChild -> {
            Text("Mammals")
        }
    }
}
