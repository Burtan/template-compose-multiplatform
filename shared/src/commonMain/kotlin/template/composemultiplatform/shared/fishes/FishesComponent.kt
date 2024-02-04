package template.composemultiplatform.shared.fishes

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.stack.webhistory.WebHistoryController
import org.kodein.di.DI
import org.kodein.di.DIAware

@OptIn(ExperimentalDecomposeApi::class)
class FishesComponent(
    context: ComponentContext,
    webHistoryController: WebHistoryController? = null,
    override val di: DI
) : FishesInterface, ComponentContext by context, DIAware {

}
