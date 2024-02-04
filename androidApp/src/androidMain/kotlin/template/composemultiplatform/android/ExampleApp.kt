package template.composemultiplatform.android

import android.app.Application
import template.composemultiplatform.shared.common.data.SqlDriverFactoryProvider
import template.composemultiplatform.shared.common.di.getMainDI
import template.composemultiplatform.shared.common.resources.Localizer
import org.kodein.di.DI
import org.kodein.di.DIAware

class ExampleApp : Application(), DIAware {
    override val di by DI.lazy {
        val mainDi = getMainDI(
            driverFactory = SqlDriverFactoryProvider(applicationContext).getDriverFactory(),
            localizer = Localizer(applicationContext)
        )
        import(mainDi)
    }
}
