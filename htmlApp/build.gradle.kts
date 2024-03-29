plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.multiplatform)
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
                implementation(npm("@material/web", "1.2.0"))
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
