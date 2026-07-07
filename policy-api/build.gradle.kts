plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ktor)
}

group = "com.example.insurance"
version = "0.1.0"

application {
    mainClass.set("com.example.insurance.policyapi.ApplicationKt")
}

dependencies {
    implementation(project(":shared"))   // riuso i DTO assicurativi
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.contentNegotiation)
    implementation(libs.ktor.serialization.json)
    implementation(libs.ktor.server.callLogging)
    implementation(libs.logback.classic)
}
