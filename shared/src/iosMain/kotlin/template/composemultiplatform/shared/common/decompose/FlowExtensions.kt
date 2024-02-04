package template.composemultiplatform.shared.common.decompose

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

fun <T : Any> StateFlow<T>.asValue(scope: CoroutineScope): Value<T> {
    val mutableValue = MutableValue(value)
    scope.launch(Dispatchers.Main) {
        this@asValue
            .filterNotNull()
            .collect {
                mutableValue.value = it
                delay(30)
            }
    }
    return mutableValue
}

fun <T : Any> Flow<T?>.asValue(scope: CoroutineScope): Value<Optional<T>> {
    val mutableValue = MutableValue(Optional<T>(null))
    scope.launch(Dispatchers.Main) {
        this@asValue
            .collect {
                mutableValue.value = Optional(it)
                delay(30)
            }
    }
    return mutableValue
}

fun <T : Any> Flow<T>.asValue(scope: CoroutineScope, initial: T): Value<T> {
    val mutableValue = MutableValue(initial)
    scope.launch(Dispatchers.Main) {
        this@asValue
            .collect {
                mutableValue.value = it
                // there is no backpressure strategy so some values could be ommited! Add delay for prevention
                delay(30)
            }
    }
    return mutableValue
}

data class Optional<T : Any>(val value: T?)
