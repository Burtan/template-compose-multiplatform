package template.composemultiplatform.shared.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.webhistory.WebHistoryController
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable
import org.kodein.di.DI
import org.kodein.di.DIAware
import template.composemultiplatform.shared.birds.BirdsComponent
import template.composemultiplatform.shared.fishes.FishesComponent
import template.composemultiplatform.shared.mammals.MammalsComponent

@OptIn(ExperimentalDecomposeApi::class)
class RootComponent(
    context: ComponentContext,
    private val deepLink: DeepLink = DeepLink.None,
    private val webHistoryController: WebHistoryController? = null,
    override val di: DI
) : RootInterface, ComponentContext by context, DIAware {

    private val nav = StackNavigation<Config>()
    private val stack = childStack(
        source = nav,
        serializer = Config.serializer(),
        initialStack = { getInitialStack(deepLink) },
        handleBackButton = true,
        childFactory = ::child,
    )
    override val childStack: Value<ChildStack<*, RootInterface.Child>> = stack

    init {
        webHistoryController?.attach(
            navigator = nav,
            stack = stack,
            getPath = ::getPathForConfig,
            serializer = Config.serializer(),
            getConfiguration = ::getConfigForPath,
        )
    }

    private fun child(config: Config, cmpContext: ComponentContext): RootInterface.Child {
        return when (config) {
            Config.Birds -> RootInterface.Child.BirdsChild(BirdsComponent(cmpContext, webHistoryController, di))
            Config.Fishes -> RootInterface.Child.FishesChild(FishesComponent(cmpContext, webHistoryController, di))
            Config.Mammals -> RootInterface.Child.MammalsChild(MammalsComponent(cmpContext, webHistoryController, di))
        }
    }

    @Serializable
    sealed interface Config {
        @Serializable data object Mammals : Config
        @Serializable data object Fishes : Config
        @Serializable data object Birds : Config
    }

    sealed interface DeepLink {
        data object None : DeepLink
        class Web(val path: String) : DeepLink
    }

    private fun getInitialStack(deepLink: DeepLink): List<Config> {
        return when (deepLink) {
            is DeepLink.None -> listOf(Config.Mammals)
            is DeepLink.Web -> listOf(getConfigForPath(deepLink.path))
        }
    }

    private fun getPathForConfig(config: Config): String {
        return when (config) {
            Config.Birds -> "/birds/"
            Config.Fishes -> "/fishes/"
            Config.Mammals -> "/mammals"
        }
    }

    private fun getConfigForPath(path: String): Config {
        return when (path.removePrefix("/")) {
            "birds" -> Config.Birds
            "fishes" -> Config.Fishes
            else -> Config.Mammals
        }
    }

}
