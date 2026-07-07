package com.example.insurance.web

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.core.App
import com.varabyte.kobweb.core.KobwebApp

/**
 * Niente Silk: solo KobwebApp per il routing. Stile = base.css (solo token/reset globali) +
 * StyleSheet Compose tipizzati montati per componente/route (chunk), non un unico uber-css.
 */
@App
@Composable
fun AppEntry(content: @Composable () -> Unit) {
    KobwebApp {
        content()
    }
}
