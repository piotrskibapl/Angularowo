plugins {
    alias(libs.plugins.jetbrains.kotlin.jvm)
}

dependencies {
    implementation(libs.rxjava)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.inject)
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kluent)
}

tasks.withType<Test> {
    useJUnitPlatform()
}
