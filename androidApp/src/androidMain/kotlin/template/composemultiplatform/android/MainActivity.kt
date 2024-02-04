package template.composemultiplatform.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.defaultComponentContext
import org.kodein.di.android.closestDI
import template.composemultiplatform.android.root.RootContent
import template.composemultiplatform.shared.root.RootComponent

class MainActivity : ComponentActivity() {

    private val di by closestDI()

    @OptIn(ExperimentalDecomposeApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val rootCmp = RootComponent(
            context = defaultComponentContext(),
            di = di
        )

        setContent {
            RootContent(rootCmp)
        }
    }

}
