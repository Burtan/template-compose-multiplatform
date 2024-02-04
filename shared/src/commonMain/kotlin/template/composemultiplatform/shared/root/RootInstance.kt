package template.composemultiplatform.shared.root

import template.composemultiplatform.shared.common.data.DaoFacadeImpl
import template.composemultiplatform.shared.common.domain.RabbitModel
import template.composemultiplatform.shared.common.resources.Localizer
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import io.ktor.client.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance

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
