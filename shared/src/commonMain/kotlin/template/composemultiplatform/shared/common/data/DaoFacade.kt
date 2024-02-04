package template.composemultiplatform.shared.common.data

import template.composemultiplatform.shared.common.domain.RabbitModel

interface DaoFacade {
    suspend fun getRabbitOrCreateById(id: String): RabbitModel
}
