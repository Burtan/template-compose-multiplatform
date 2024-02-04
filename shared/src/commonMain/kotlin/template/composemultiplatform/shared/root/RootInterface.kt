package template.composemultiplatform.shared.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import template.composemultiplatform.shared.birds.BirdsInterface
import template.composemultiplatform.shared.fishes.FishesInterface
import template.composemultiplatform.shared.mammals.MammalsInterface

interface RootInterface {
    val childStack: Value<ChildStack<*, Child>>

    sealed class Child {
        data class BirdsChild(val birdsCmp: BirdsInterface) : Child()
        data class FishesChild(val fishesCmp: FishesInterface) : Child()
        data class MammalsChild(val mammalsCmp: MammalsInterface) : Child()
    }
}
