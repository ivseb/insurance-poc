plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ktor)
}

group = "com.example.insurance"
version = "0.1.0"

application {
    mainClass.set("com.example.insurance.bff.ApplicationKt")
}

dependencies {
    implementation(project(":shared"))   // riuso DTO + ClaimValidation (stessa validazione del frontend)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.contentNegotiation)
    implementation(libs.ktor.serialization.json)
    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.authJwt)
    implementation(libs.auth0.jwt)
    implementation(libs.ktor.server.cors)
    implementation(libs.ktor.server.statusPages)
    implementation(libs.ktor.server.callLogging)
    implementation(libs.ktor.server.defaultHeaders)
    // client per le chiamate s2s in uscita (verso Strapi / API polizze)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.contentNegotiation)
    implementation(libs.logback.classic)
    // OpenTelemetry: instrumentation nativa Ktor (server+client) + JVM + log
    implementation(platform(libs.otel.bom.alpha))
    implementation(libs.otel.ktor)
    implementation(libs.otel.sdk.autoconfigure)
    implementation(libs.otel.exporter.otlp)
    implementation(libs.otel.runtime)
    implementation(libs.otel.logback)
}
