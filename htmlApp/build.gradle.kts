import com.github.jk1.license.render.JsonReportRenderer
import org.apache.tools.ant.taskdefs.condition.Os
import org.jetbrains.kotlin.gradle.targets.js.ir.KotlinJsIrLink
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootPlugin

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.kotlin.plugin.compose)
    alias(libs.plugins.licenses)
    alias(libs.plugins.moko.resources)
}

kotlin {
    jvmToolchain(17)

    js(IR) {
        browser {
            testTask {
                useKarma {
                    useChromeHeadlessNoSandbox()
                    //useFirefoxHeadless()
                    //useSafari()
                }
            }
        }
        binaries.executable()
    }

    sourceSets {
        jsMain {
            dependencies {
                implementation(compose.html.core)
                implementation(compose.runtime)
                implementation(libs.wrappers.js)
                implementation(libs.wrappers.web)
                //implementation(libs.decompose.compose)

                // material web
                implementation(npm("@material/mwc-top-app-bar-fixed", "0.27.0"))
                implementation(npm("@material/mwc-snackbar", "0.27.0"))
                implementation(npm("@material/web", "1.5.0"))
                implementation(npm("@material-symbols/font-400", "0.14.0"))
                implementation(npm("sass-loader", "14.1.0"))
                implementation(npm("sass", "1.70.0"))
                implementation(npm("@fontsource/roboto", "5.0.8"))

                implementation(devNpm("copy-webpack-plugin", "12.0.2"))

                implementation(project(":shared"))
            }
        }
        jsTest {
            dependencies {
                implementation(libs.kotlin.test)
                implementation(compose.html.testUtils)

                // coroutines
                implementation(libs.coroutines.test)

                implementation(npm("karma-viewport", "1.0.9"))
                implementation(npm("karma-snapshot", "0.6.0"))
                implementation(npm("karma-mocha-snapshot", "0.2.1"))
                implementation(npm("karma-mocha-reporter", "2.2.5"))
                implementation(npm("chai", "4.3.7"))
                implementation(npm("chai-karma-snapshot", "0.8.0"))
            }
        }
    }
}

licenseReport {
    // TODO npm is partly missing
    configurations = arrayOf(
        "jsRuntimeClasspath",
        "jsNpmAggregated",
    )
    renderers = arrayOf(JsonReportRenderer())
    //importers = arrayOf(NpxLicenseCheckerImporter("PathoShare web", listOf("/home/frederik/IdeaProjects/pathoshare-app/build/js/")))
}

multiplatformResources {
    resourcesPackage.set("app.pathoshare.web")
    resourcesClassName = "WebRes"
}

val copyWebLicenses = tasks.create<Copy>("copyWebLicenses") {
    val licenseTask = tasks.getByName("generateLicenseReport")
    val webLicensesDir = "${layout.buildDirectory.get()}/generated/webLicenses/"

    // the task's configuration
    from(licenseTask)
    into("$webLicensesDir/files")

    // output must be the base directory with the dir "files"
    outputs.dir(webLicensesDir)

    rename {
        it.replace("index", "webLicenses")
    }
}

// TODO
// somehow includes processTestResources folder
tasks.getByName<KotlinJsIrLink>("compileTestDevelopmentExecutableKotlinJs") {}
// does not include processResources folder
tasks.getByName<KotlinJsIrLink>("compileDevelopmentExecutableKotlinJs") {}

afterEvaluate {
    multiplatformResources.resourcesSourceSets.getByName("jsMain").srcDir(copyWebLicenses)
}

/**
 * npm downloaded by kotlin is incompatible with alpine linux
 */
plugins.withType(NodeJsRootPlugin::class) {
    (project.extensions["kotlinNodeJs"] as NodeJsRootExtension)
        .apply {
            if (Os.isFamily(Os.FAMILY_UNIX)) {
                download = false
            }
        }
}

/**
 * Kotlin multiplatform does not copy main resources to js test folder. Do it manually.
 */
tasks.getByName("jsTestProcessResources", Copy::class) {
    val resMain = tasks.getByName("jsProcessResources")

    from(resMain)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
