package template.composemultiplatform.shared.common.data

import template.composemultiplatform.shared.common.domain.RabbitModel

class DaoFacadeImpl(private val driverFactory: DriverFactory) : DaoFacade {

    private val sharedDatabase = SharedDatabase(driverFactory)

    override suspend fun getRabbitOrCreateById(id: String) = sharedDatabase.invoke {
        return@invoke RabbitModel()
        /*val rabbitQueryData = if (driverFactory.isAsync()) {
            rabbitQueries
                .byId(id)
                .awaitAsList()
        } else {
            rabbitQueries
                .byId(id)
                .executeAsList()
        }

        if (rabbitQueryData.isEmpty()) {
            val startDate = Clock.System.now().toEpochMilliseconds()
            rabbitQueries.insert(Cases(id, null, startDate))
            CaseModel(
                id = id,
                startDate = startDate
            )
        } else {
            rabbitQueryData.toCase()
        }*/
    }

}
