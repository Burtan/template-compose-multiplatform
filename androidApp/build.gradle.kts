import java.io.ByteArrayOutputStream

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.paparazzi)
}

kotlin {
    jvmToolchain(17)

    androidTarget()

    sourceSets {
        androidMain {
            dependencies {
                implementation(libs.androidx.compose.ui.tooling)
                implementation(compose.material3)

                // androidx extensions
                implementation(libs.androidx.activity.compose)

                // decompose
                implementation(libs.decompose.compose)

                implementation(project(":shared"))
            }
        }
    }
}

android {
    namespace = "template.composemultiplatform.android"
    compileSdk = 34
    defaultConfig {
        applicationId = "template.composemultiplatform.android"
        minSdk = 21
        versionCode = getVersionCodeFromGit()
        versionName = getVersionNameFromGit()
    }
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
    dependencies {
        testImplementation(libs.junit)
        testImplementation(libs.robolectric)
        testImplementation(libs.androidx.compose.ui.test.junit4)
        testImplementation(libs.kotest.assertions.core)
        testImplementation(libs.coroutines.test)
        testImplementation(libs.hamcrest)
    }
}

fun getVersionCodeFromGit() : Int {
    return try {
        val code = ByteArrayOutputStream()
        exec {
            commandLine("git", "tag", "--list")
            standardOutput = code
        }
        code.toString().split("\n").size
    }
    catch (ignored: Exception) {
        1
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
