import com.varabyte.kobweb.gradle.application.util.configAsKobwebApplication
import kotlinx.html.link
import kotlinx.html.script
import kotlinx.html.unsafe

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kobweb.application)
}

group = "com.example.insurance.web"
version = "0.1.0"

kobweb {
    app {
        index {
            description.set("Insurance POC — Compose HTML")
            // design system custom servito come CSS nativo (perf + accessibilità)
            head.add {
                // Tipografia come ACME: Source Sans 3 (body) + Source Serif 4 (titoli,
                // sostituto libero di Publico Headline, che è a pagamento).
                link { href = "https://fonts.googleapis.com"; rel = "preconnect" }
                link { href = "https://fonts.gstatic.com"; rel = "preconnect"; attributes["crossorigin"] = "" }
                link {
                    href = "https://fonts.googleapis.com/css2?family=Source+Sans+3:wght@400;600;700;800&family=Source+Serif+4:opsz,wght@8..60,600;8..60,700&display=swap"
                    rel = "stylesheet"
                }
                // Web Awesome: componenti accessibili (a11y già risolta dalla libreria)
                link { href = "https://cdn.jsdelivr.net/npm/@awesome.me/webawesome@3.9.0/dist-cdn/styles/webawesome.css"; rel = "stylesheet" }
                // Layer BASE (solo token, reset, --wa-*, keyframes). I componenti/pagine
                // portano i propri stili via StyleSheet Compose montati per route (chunk).
                link { href = "/base.css"; rel = "stylesheet" }
                // Autoloader Web Awesome: registra i custom element <wa-*> on-demand
                script {
                    src = "https://cdn.jsdelivr.net/npm/@awesome.me/webawesome@3.9.0/dist-cdn/webawesome.loader.js"
                    attributes["type"] = "module"
                }
                // Fallback frame-clock per tab nascoste/headless (no-op in browser visibile).
                script {
                    unsafe {
                        +"""
                        if (document.hidden) {
                          window.requestAnimationFrame = function(cb){ return setTimeout(function(){ cb(performance.now()); }, 16); };
                          window.cancelAnimationFrame = function(id){ clearTimeout(id); };
                        }
                        """.trimIndent()
                    }
                }
            }
        }
    }
}

kotlin {
    configAsKobwebApplication("insurance")
    sourceSets {
        jsMain.dependencies {
            implementation(project(":shared"))
            implementation(project(":designsystem"))   // componenti UI riusabili
            implementation(libs.compose.runtime)
            implementation(libs.compose.html.core)
            implementation(libs.kobweb.core)
            implementation(libs.kotlinx.coroutines.core)
        }
    }
}
