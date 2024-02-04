package template.composemultiplatform.shared.common.data

expect class SqlDriverFactoryProvider {
    fun getDriverFactory(name: String = "Example.db"): DriverFactory
}
