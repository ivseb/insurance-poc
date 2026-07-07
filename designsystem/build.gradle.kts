import com.varabyte.kobweb.gradle.library.util.configAsKobwebLibrary

/**
 * Design system: componenti frontend riusabili (Compose HTML + Web Awesome).
 * NON conosce le rotte dell'app, il BFF o le @Page: è consumato da :web.
 */
plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kobweb.library)
}

group = "com.example.insurance.designsystem"
version = "0.1.0"

kotlin {
    configAsKobwebLibrary()
    sourceSets {
        jsMain.dependencies {
            implementation(project(":shared"))
            implementation(libs.compose.runtime)
            implementation(libs.compose.html.core)
            implementation(libs.kobweb.core)
        }
    }
}
