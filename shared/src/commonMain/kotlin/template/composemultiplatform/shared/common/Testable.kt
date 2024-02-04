package template.composemultiplatform.shared.common

import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.reflect.KFunction

interface Testable {
    val testTitle: String
    val actionsFlow: MutableStateFlow<KFunction<*>?>
}
