plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.android.library)
}

kotlin {
    jvm()           // consumato dal BFF (riuso dei DTO -> single source of truth)
    androidTarget()
    js(IR) {        // consumato dal web (Compose HTML / Kotlin/JS)
        browser()
        // niente binaries.executable(): è una libreria, l'app è il modulo :web
    }

    sourceSets {
        commonMain.dependencies {
            // api: il companion serializzabile dei DTO (es. enum PolicyStatus) deve essere
            // visibile ai consumer (:web, :android, :bff)
            api(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.contentNegotiation)
            implementation(libs.ktor.serialization.json)
        }
        jvmMain.dependencies { implementation(libs.ktor.client.okhttp) }
        androidMain.dependencies { implementation(libs.ktor.client.okhttp) }
        jsMain.dependencies { implementation(libs.ktor.client.js) }
    }
}

android {
    namespace = "com.example.insurance.shared"
    compileSdk = 35
    defaultConfig { minSdk = 24 }
}
