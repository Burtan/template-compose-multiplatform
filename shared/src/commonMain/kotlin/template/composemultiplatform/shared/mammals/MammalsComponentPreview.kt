package template.composemultiplatform.shared.mammals

import kotlinx.coroutines.flow.MutableStateFlow
import template.composemultiplatform.shared.common.Testable
import kotlin.reflect.KFunction

class MammalsComponentPreview(
    override val testTitle: String = "Loading mammals",
) : MammalsInterface, Testable {
    override val actionsFlow = MutableStateFlow<KFunction<*>?>(null)

    companion object {
        val previews = arrayOf(
            MammalsComponentPreview(),
        )
    }
}
