package template.composemultiplatform.shared.root

import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import io.ktor.client.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance
import template.composemultiplatform.shared.common.data.DaoFacadeImpl
import template.composemultiplatform.shared.common.resources.Localizer

class RootInstance(override val di: DI) : InstanceKeeper.Instance, DIAware {

    private val scope = CoroutineScope(Dispatchers.Default)
    private val httpClient by instance<HttpClient>()
    private val dao by instance<DaoFacadeImpl>()
    private val localizer by instance<Localizer>()

    init {

    }

    override fun onDestroy() {
        scope.cancel()
    }
}
