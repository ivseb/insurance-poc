rootProject.name = "insurance-poc"

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()   // Kobweb 0.24+ è pubblicato qui
        google()
    }
}

include(":shared")       // KMP: modelli, design token, client BFF, validazioni (web + android + bff)
include(":designsystem") // Componenti UI riusabili (Compose HTML + Web Awesome) — il design system
include(":designsystem-preview") // Compose Desktop: anteprima @Preview dei componenti dentro IntelliJ
include(":policy-api")   // Backend fittizio: simula l'API assicurativa (polizze/sinistri)
include(":bff")          // Ktor: backend-for-frontend, unica porta verso CMS e API polizze (s2s)
include(":web")          // Kobweb / Compose HTML (Kotlin/JS, DOM reale)
include(":androidApp")   // Compose Android
