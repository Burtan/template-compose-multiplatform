package template.composemultiplatform.shared.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.MutableValue
import kotlinx.coroutines.flow.MutableStateFlow
import template.composemultiplatform.shared.common.Testable
import template.composemultiplatform.shared.mammals.MammalsComponentPreview
import kotlin.reflect.KFunction

class RootComponentPreview(
    override val testTitle: String = "Root",
    childStack: ChildStack<*, RootInterface.Child> = mammalsStack,
) : RootInterface, Testable {
    override val childStack = MutableValue(childStack)
    override val actionsFlow = MutableStateFlow<KFunction<*>?>(null)

    companion object {
        private val mammalsStack = ChildStack(
            configuration = RootComponent.Config.Mammals,
            instance = RootInterface.Child.MammalsChild(MammalsComponentPreview())
        )

        val previews = arrayOf(
            RootComponentPreview(),
        )
    }
}
