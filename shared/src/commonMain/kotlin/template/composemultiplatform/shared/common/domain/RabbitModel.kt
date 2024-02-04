package template.composemultiplatform.shared.common.domain

import kotlinx.serialization.Serializable

@Serializable
data class RabbitModel(
    val id: Long = 0,
    val name: String = "Harry",
) {
    companion object {
        fun map(id: Long, name: String): RabbitModel {
            return RabbitModel(
                id = id,
                name = name,
            )
        }
    }
}
