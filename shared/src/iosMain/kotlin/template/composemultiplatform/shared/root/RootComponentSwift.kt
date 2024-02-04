package template.composemultiplatform.shared.root

import template.composemultiplatform.shared.common.data.SqlDriverFactoryProvider
import template.composemultiplatform.shared.common.decompose.asValue
import template.composemultiplatform.shared.common.di.getMainDI
import template.composemultiplatform.shared.common.resources.Localizer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import org.kodein.di.DI
import template.composemultiplatform.shared.birds.BirdsInterface

class RootComponentSwift(component: RootInterface) : RootInterface by component {

    companion object {
        fun getDi(): DI {
            return DI {
                val mainDi = getMainDI(
                    driverFactory = SqlDriverFactoryProvider().getDriverFactory("Example.db"),
                    localizer = Localizer()
                )
                import(mainDi)
            }
        }
    }
}
