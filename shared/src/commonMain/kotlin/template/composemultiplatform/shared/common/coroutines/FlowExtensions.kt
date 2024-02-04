package template.composemultiplatform.shared.common.coroutines

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlin.time.Duration

/**
 * Keeps each item for the duration
 */
fun <T: Any> Flow<Collection<T>>.aggregate(duration: Duration): Flow<Collection<T>> = flow {
    val aggregatedItems = mutableSetOf<T>()
    collect {
        val newItems = it.minus(aggregatedItems)
        coroutineScope {
            launch {
                delay(duration)
                aggregatedItems.removeAll(newItems.toSet())
            }
        }
        aggregatedItems.addAll(it)
        emit(aggregatedItems)
    }
}