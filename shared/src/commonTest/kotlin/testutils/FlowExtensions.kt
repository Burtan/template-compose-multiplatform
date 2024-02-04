package testutils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

suspend inline fun <reified T> Flow<*>.awaitStatus(): T {
    val status = this
        .map {
            if (it is T) {
                it
            } else {
                null
            }
        }
        .filterNotNull()
        .first()

    return status
}