package template.composemultiplatform.shared.birds

import kotlinx.coroutines.flow.MutableStateFlow
import template.composemultiplatform.shared.common.Testable
import kotlin.reflect.KFunction

class BirdsComponentPreview(
    override val testTitle: String = "Loading root",
) : BirdsInterface, Testable {
    override val actionsFlow = MutableStateFlow<KFunction<*>?>(null)

    companion object {
        val previews = arrayOf(
            BirdsComponentPreview(),
        )
    }
}
