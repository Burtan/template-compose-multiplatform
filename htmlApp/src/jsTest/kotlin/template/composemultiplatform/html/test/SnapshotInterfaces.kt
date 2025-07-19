@file:JsModule("karma-snapshot")
@file:JsNonModule

package template.composemultiplatform.html.test

external interface Snapshot {
    val lang: String?
    val code: String
}

external interface SnapshotState {
    val update: Boolean
    fun get(path: Array<String>, index: Int): Snapshot?
    fun set(path: Array<String>, index: Int, code: String, lang: String? = definedExternally)
    fun match(received: String, expected: String): Boolean
}
