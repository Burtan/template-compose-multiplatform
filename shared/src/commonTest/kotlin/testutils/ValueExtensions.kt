package testutils

import com.arkivanov.decompose.value.Value
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend inline fun <reified T> Value<*>.awaitStatus(): T {
    return suspendCoroutine {
        subscribe { value ->
            if (value is T) {
                it.resume(value)
            }
        }
    }
}