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
    // OpenTelemetry: instrumentation nativa Ktor server + JVM + log
    implementation(platform(libs.otel.bom.alpha))
    implementation(libs.otel.ktor)
    implementation(libs.otel.sdk.autoconfigure)
    implementation(libs.otel.exporter.otlp)
    implementation(libs.otel.runtime)
    implementation(libs.otel.logback)
}
