import org.apache.tools.ant.taskdefs.condition.Os
import org.jetbrains.compose.ExperimentalComposeLibrary

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.kotlin.plugin.serialization)
    alias(libs.plugins.kotlin.native.cocoapods)
    alias(libs.plugins.sqldelight)
    alias(libs.plugins.moko.resources)
}

kotlin {
    jvmToolchain(17)

    androidTarget()
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    js(IR) {
        browser {
            testTask {
                useKarma {
                    useSourceMapSupport()
                    if (!Os.isFamily(Os.FAMILY_MAC)) {
                        useFirefoxNightlyHeadless()
                        useChromiumHeadless()
                        useSafari()
                    } else {
                        useFirefoxHeadless()
                    }
                }
            }
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlinx.datetime)
                implementation(libs.uuid)
                implementation(libs.sqldelight.coroutines)

                implementation(compose.foundation)
                @OptIn(ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)

                api(libs.decompose)
                api(libs.coroutines.core)
                api(libs.ktor.client.core)
                api(libs.ktor.client.contentNegotiation)
                api(libs.ktor.client.logging)
                api(libs.ktor.json)
                api(libs.kodein.di)
                api(libs.moko.resources)
            }
        }
        commonTest {
            dependencies {
                implementation(libs.kotest.assertions.core)
                implementation(libs.kotlin.test)

                implementation(libs.coroutines.test)
                implementation(libs.ktor.client.mock)
                implementation(libs.ktor.client.auth)
            }
        }

        androidMain.dependencies {
            implementation(libs.sqldelight.android)
            implementation(libs.ktor.client.android)
            api(libs.kodein.android)
            api(libs.moko.resources.compose)
        }

        val androidUnitTest by getting
        androidUnitTest.dependencies {
            implementation(libs.junit)
            implementation(libs.robolectric)
        }

        jsMain.dependencies {
            implementation(libs.sqldelight.webWorker)
            implementation(npm("sql.js", "1.10.2"))
            implementation(npm("@sqlite.org/sqlite-wasm", "3.45.1-build1"))
            implementation(npm("@cashapp/sqldelight-sqljs-worker", "2.0.1"))
            implementation(devNpm("copy-webpack-plugin", "12.0.2"))
            // ktor
            api(libs.ktor.client.js)
        }
        jsTest.dependencies {
            implementation(npm("karma-safarinative-launcher", "1.1.0"))
        }

        iosMain.dependencies {
            implementation(libs.sqldelight.native)
            implementation(libs.ktor.client.ios)
        }
    }

    cocoapods {
        name = "Shared"
        version = "0.1.0"
        summary = "Shared code"
        homepage = "None"
        ios.deploymentTarget = "14.7"
        podfile = project.file("../iosApp/Podfile")
        framework {
            isStatic = false // SwiftUI preview requires a dynamic framework
            baseName = "Shared"
            // decompose navigation
            export("com.arkivanov.decompose:decompose:2.2.2")
            export("com.arkivanov.essenty:lifecycle:1.3.0")

            // shared resources
            export("dev.icerock.moko:resources:0.24.0-alpha-2")
            export("dev.icerock.moko:graphics:0.9.0") // toUIColor here
        }
    }
}

sqldelight {
    databases {
        create("ExampleDatabase") {
            packageName.set("template.composemultiplatform.shared")
            generateAsync.set(true)
        }
    }
}

multiplatformResources {
    resourcesPackage = "template.composemultiplatform.shared"
}

android {
    namespace = "template.composemultiplatform.shared"
    compileSdk = 34
    defaultConfig {
        minSdk = 21
    }
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}
