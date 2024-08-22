pluginManagement {
    repositories {
        google()
        mavenCentral()
        maven { url = java.net.URI("https://jitpack.io") }
        gradlePluginPortal()
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "com.hyperdevs.poeditor") {
                useModule("com.github.hyperdevs-team:poeditor-android-gradle-plugin:${requested.version}")
            } else if (requested.id.id == "androidx.navigation.safeargs.plugin") {
                useModule("androidx.navigation:navigation-safe-args-gradle-plugin:${requested.version}")
            }
        }
    }
}

include(":data")
include(":domain")
include(":app")
