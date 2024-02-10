import com.github.jk1.license.render.JsonReportRenderer
import org.apache.tools.ant.taskdefs.condition.Os
import org.jetbrains.compose.ExperimentalComposeLibrary
import java.io.ByteArrayOutputStream
import java.util.*

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.kotlin.plugin.serialization)
    alias(libs.plugins.kotlin.native.cocoapods)
    alias(libs.plugins.sqldelight)
    alias(libs.plugins.moko.resources)
    alias(libs.plugins.licenses)
}

version = getVersionNameFromGit()

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
            resources.srcDir("${layout.buildDirectory.get()}/generated/licenses/")
            resources.srcDir("${layout.buildDirectory.get()}/generated/buildInfo/")

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


// generates licenses for iOS as it has no own module with Gradle
licenseReport {
    configurations = arrayOf(
        "iosArm64CompileKlibraries",
        "iosArm64CompilationApi",
    )
    renderers = arrayOf(JsonReportRenderer())
    outputDir = File("${layout.buildDirectory.get()}/generated/licenses/").absolutePath
}

val createBuildInfoTaks = tasks.create("copyBuildInfo") {
    // the task's configuration
    val versionFile = File("${layout.buildDirectory.get()}/generated/buildInfo/version.txt")
    val yearFile = File("${layout.buildDirectory.get()}/generated/buildInfo/year.txt")
    outputs.files(yearFile.absolutePath, versionFile.absolutePath)

    // the task's action
    doLast {
        yearFile.writeText(Calendar.getInstance().get(Calendar.YEAR).toString())
        versionFile.parentFile.mkdirs()
        versionFile.writeText(version.toString())
    }
}
val licenseTask = tasks.getByName("generateLicenseReport")

// configure build dependencies
afterEvaluate {
    listOf(licenseTask, createBuildInfoTaks).forEach {
        tasks.getByName("metadataProcessResources").dependsOn(it)
        tasks.getByName("jsProcessResources").dependsOn(it)
        tasks.getByName("iosArm64ProcessResources").dependsOn(it)
        tasks.getByName("iosSimulatorArm64ProcessResources").dependsOn(it)
        tasks.getByName("iosX64ProcessResources").dependsOn(it)
        tasks.getByName("processDebugJavaRes").dependsOn(it)
        tasks.getByName("processReleaseJavaRes").dependsOn(it)

        // TODO this is bugged, they should run AFTER license/buildInfo tasks
        it.mustRunAfter("metadataCommonMainProcessResources")
        it.mustRunAfter("metadataNativeMainProcessResources")
        it.mustRunAfter("metadataIosMainProcessResources")
        it.mustRunAfter("metadataAppleMainProcessResources")
    }
}

fun getVersionNameFromGit() : String {
    return try {
        val stdout = ByteArrayOutputStream()
        exec {
            commandLine("git", "describe", "--tags", "--dirty")
            standardOutput = stdout
        }
        stdout.toString().trim()
    }
    catch (ignored: Exception) {
        ""
    }
}
