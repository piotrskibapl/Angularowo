plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.jetbrains.kotlin.kapt)
}

kotlin {
    jvmToolchain(17)
}

android {
    namespace = "pl.piotrskiba.angularowo.data"
    compileSdk = project.properties["androidCompileSdkVersion"].toString().toInt()
    defaultConfig {
        minSdk = project.properties["androidMinSdkVersion"].toString().toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        multiDexEnabled = true
    }
    buildTypes {
        getByName("release") {
            buildConfigField("String", "API_KEY", "\"${project.properties["apiKey"]}\"")
        }
        getByName("debug") {
            buildConfigField("String", "API_KEY", "\"${project.properties["apiKey"]}\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation(project(":domain"))

    implementation(libs.rxjava)
    implementation(libs.rxandroid)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.retrofit.adapter.rxjava3)
    implementation(libs.dagger.android)
    implementation(libs.dagger.android.support)
    kapt(libs.dagger.android.processor)
    kapt(libs.dagger.compiler)
    implementation(libs.okhttp)
    implementation(libs.okhttp.interceptor.logging)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.config)
    implementation(libs.androidx.room)
    implementation(libs.androidx.room.rxjava)
    kapt(libs.androidx.room.compiler)
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kluent)
}

tasks.withType<Test> {
    useJUnitPlatform()
}
