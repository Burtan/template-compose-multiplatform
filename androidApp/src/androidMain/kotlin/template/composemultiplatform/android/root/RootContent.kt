package template.composemultiplatform.android.root

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import com.arkivanov.decompose.extensions.compose.jetpack.subscribeAsState
import template.composemultiplatform.android.birds.BirdsContent
import template.composemultiplatform.android.fishes.FishesContent
import template.composemultiplatform.android.mammals.MammalsContent
import template.composemultiplatform.shared.root.RootComponentPreview
import template.composemultiplatform.shared.root.RootInterface

@Composable
fun RootContent(
    rootCmp: RootInterface,
) {
    val stack by rootCmp.childStack.subscribeAsState()

    Scaffold(
        snackbarHost = {
            val snackbarHostState = remember { SnackbarHostState() }
            LaunchedEffect(snackbarHostState) {

            }
        }
    ) { padding ->
        Children(
            stack = stack,
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            when (val child = it.instance) {
                is RootInterface.Child.BirdsChild -> BirdsContent(child.birdsCmp)
                is RootInterface.Child.FishesChild -> FishesContent(child.fishesCmp)
                is RootInterface.Child.MammalsChild -> MammalsContent(child.mammalsCmp)
            }
        }
    }
}

@Composable
@Preview(locale = "de")
fun RootContentPreview(
    @PreviewParameter(RootComponentProvider::class) rootCmp: RootComponentPreview,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        RootContent(rootCmp)
    }
}
