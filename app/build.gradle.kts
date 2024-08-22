plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.jetbrains.kotlin.kapt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.firebase.perf)
    alias(libs.plugins.poeditor)
    id("androidx.navigation.safeargs.kotlin")
}

kotlin {
    jvmToolchain(17)
}

poEditor {
    apiToken.set(project.properties["poEditorApiToken"].toString())
    projectId.set(project.properties["poEditorProjectId"].toString().toInt())
    defaultLang.set("pl")
    filters.set(listOf("translated"))
    unquoted.set(true)
}

android {
    namespace = "pl.piotrskiba.angularowo"
    compileSdk = project.properties["androidCompileSdkVersion"].toString().toInt()
    defaultConfig {
        applicationId = "pl.piotrskiba.angularowo"
        minSdk = project.properties["androidMinSdkVersion"].toString().toInt()
        targetSdk = project.properties["androidTargetSdkVersion"].toString().toInt()
        versionCode = 77
        versionName = "4.0.1"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        multiDexEnabled = true
    }
    signingConfigs {
        create("release") {
            keyAlias = project.properties["signingKeyAlias"].toString()
            keyPassword = project.properties["signingKeyPass"].toString()
            storeFile = file(project.properties["signingStoreFilePath"].toString())
            storePassword = project.properties["signingStorePass"].toString()
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
        }
        getByName("debug") {
            ext.set("enableCrashlytics", false)
        }
    }
    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":data"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.preference)
    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.dagger.android)
    implementation(libs.dagger.android.support)
    kapt(libs.dagger.android.processor)
    kapt(libs.dagger.compiler)
    implementation(libs.androidx.recyclerview)
    implementation(libs.gson)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.okhttp.interceptor.logging)
    implementation(libs.okhttp.tls)
    implementation(libs.glide)
    ksp(libs.glide.compiler)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.perf)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.inappmessaging.display)
    implementation(libs.firebase.config)
    implementation(libs.pinentryedittext)
    implementation(libs.admob)
    implementation(libs.androidx.multidex)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.viewmodel.savedstate)
    implementation(libs.androidx.lifecycle.livedata)
    implementation(libs.androidx.room)
    ksp(libs.androidx.room.compiler)
    implementation(libs.rxjava)
    implementation(libs.rxandroid)
    implementation(libs.bindingcollectionadapter)
    implementation(libs.bindingcollectionadapter.recyclerview)
    implementation(libs.androidx.work)
    implementation(libs.materialshowcase)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)
    implementation(libs.inappupdates)
    implementation(libs.inappupdates.ktx)
    implementation(libs.androidx.biometric)
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kluent)
}

tasks.withType<Test> {
    useJUnitPlatform()
}
