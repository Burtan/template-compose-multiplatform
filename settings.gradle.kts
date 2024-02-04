rootProject.name = "template-compose-multiplatform"
include(":shared")
include(":androidApp")
include(":htmlApp")

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
    }
}
