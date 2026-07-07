import org.jetbrains.compose.desktop.application.dsl.TargetFormat

/**
 * Modulo Compose DESKTOP (solo JVM): anteprima @Preview dei componenti dentro IntelliJ
 * (plugin "Compose Multiplatform IDE Support"), senza Android Studio.
 *
 * Usa kotlin("jvm") semplice (non Multiplatform): un solo source set, niente glitch
 * di indicizzazione dell'IDE. I componenti web sono Compose HTML (non eseguibili su
 * Desktop): qui ci sono composable Desktop che rispecchiano il design system coi token (:shared).
 */
plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.jetbrains.compose)
}

group = "com.example.insurance.preview"
version = "0.1.0"

dependencies {
    implementation(project(":shared"))
    implementation(compose.desktop.currentOs)
    implementation(compose.material3)
    implementation(compose.components.uiToolingPreview)
}

compose.desktop {
    application {
        mainClass = "com.example.insurance.preview.MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg)
            packageName = "DesignSystemPreview"
        }
    }
}
