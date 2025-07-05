import com.github.jk1.license.render.JsonReportRenderer
import org.apache.tools.ant.taskdefs.condition.Os
import java.util.*

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.kotlin.plugin.serialization)
    alias(libs.plugins.kotlin.plugin.compose)
    alias(libs.plugins.kotlin.native.cocoapods)
    alias(libs.plugins.sqldelight)
    alias(libs.plugins.moko.resources)
    alias(libs.plugins.licenses)
}

version = getVersionNameFromGit()

kotlin {
    jvmToolchain(21)

    androidTarget()
    jvm()
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
                implementation(libs.sqldelight.coroutines)

                implementation(compose.foundation)

                api(libs.decompose)
                api(libs.decompose.compose)

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
        androidUnitTest {
            // must use an android test SourceSet!
            android.sourceSets.getByName("test").resources.srcDir("src/commonTest/resources")
            dependencies {
                implementation(libs.junit)
                implementation(libs.robolectric)
            }
        }

        jsMain.dependencies {
            api(libs.sqldelight.webWorker)
            implementation(npm("@sqlite.org/sqlite-wasm", "3.45.1-build1"))
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

        jvmMain.dependencies {
            implementation(libs.sqldelight.jdbc)
            implementation(libs.ktor.client.java)
        }
    }

//    cocoapods {
//        name = "Shared"
//        version = "0.1.0"
//        summary = "Shared code"
//        homepage = "None"
//        ios.deploymentTarget = "14.7"
//        podfile = project.file("../iosApp/Podfile")
//        framework {
//            isStatic = false // SwiftUI preview requires a dynamic framework
//            baseName = "Shared"
//            // decompose navigation
//            export(libs.decompose.asProvider().get().toString())
//            export("com.arkivanov.essenty:lifecycle:2.2.1")
//
//            // shared resources
//            export(libs.moko.resources.asProvider().get().toString())
//            export("dev.icerock.moko:graphics:0.9.0") // toUIColor here
//        }
//    }
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
    resourcesPackage.set("template.composemultiplatform.shared")
    resourcesClassName.set("SharedRes")
}

android {
    namespace = "template.composemultiplatform.shared"
    compileSdk = 35
    defaultConfig {
        minSdk = 21
    }
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}


// generates licenses for iOS as it has no own module with Gradle
licenseReport {
    configurations = arrayOf(
        "iosArm64CompileKlibraries"
    )
    renderers = arrayOf(JsonReportRenderer())
}

val createBuildInfo = tasks.register("createBuildInfo") {
    val buildInfoDir = "${layout.buildDirectory.get()}/generated/buildInfo/"

    // the task's configuration
    outputs.dir(buildInfoDir)

    // the task's action
    doLast {
        val versionFile = File("$buildInfoDir/files/version.txt")
        versionFile.parentFile.mkdirs()
        versionFile.writeText(version.toString())
        val yearFile = File("$buildInfoDir/files/year.txt")
        yearFile.parentFile.mkdirs()
        yearFile.writeText(Calendar.getInstance().get(Calendar.YEAR).toString())
    }
}

val copyIosLicenses = tasks.register<Copy>("copyIosLicenses") {
    val licenseTask = tasks.getByName("generateLicenseReport")
    val iosLicensesDir = "${layout.buildDirectory.get()}/generated/iosLicenses"

    // the task's configuration
    from(licenseTask)
    into("$iosLicensesDir/files")

    // output must be the base directory with the dir "files"
    outputs.dir(iosLicensesDir)

    rename {
        it.replace("index", "iosLicenses")
    }
}

afterEvaluate {
    multiplatformResources.resourcesSourceSets["commonMain"].srcDir(createBuildInfo)
    multiplatformResources.resourcesSourceSets["iosMain"].srcDir(copyIosLicenses)
}

fun getVersionNameFromGit() : String {
    val result = providers.exec {
        commandLine("git", "describe", "--tags", "--dirty")
    }
    return result.standardOutput.asText.get().trim()
}
