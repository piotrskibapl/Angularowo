plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.jetbrains.kotlin.kapt)
}

kotlin {
    jvmToolchain(17)
}

android {
    namespace = "pl.piotrskiba.angularowo.domain"
    compileSdk = project.properties["androidCompileSdkVersion"].toString().toInt()
    defaultConfig {
        minSdk = project.properties["androidMinSdkVersion"].toString().toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        multiDexEnabled = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation(libs.rxjava)
    implementation(libs.rxandroid)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.dagger.android)
    implementation(libs.dagger.android.support)
    kapt(libs.dagger.android.processor)
    kapt(libs.dagger.compiler)
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kluent)
}

tasks.withType<Test> {
    useJUnitPlatform()
}
