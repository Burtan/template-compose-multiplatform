package template.composemultiplatform.shared.fishes

import kotlinx.coroutines.flow.MutableStateFlow
import template.composemultiplatform.shared.common.Testable
import kotlin.reflect.KFunction

class FishesComponentPreview(
    override val testTitle: String = "Loading fishes",
) : FishesInterface, Testable {
    override val actionsFlow = MutableStateFlow<KFunction<*>?>(null)

    companion object {
        val previews = arrayOf(
            FishesComponentPreview(),
        )
    }
}
