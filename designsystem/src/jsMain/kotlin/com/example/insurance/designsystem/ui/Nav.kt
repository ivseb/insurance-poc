package com.example.insurance.designsystem.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Text

/**
 * Astrazione di navigazione: i componenti del design system non conoscono Kobweb.
 * L'app fornisce l'implementazione via CompositionLocal (page-adapter lato Kobweb).
 */
val LocalNavigate = compositionLocalOf<(String) -> Unit> { {} }

@Composable
fun AppLink(path: String, className: String? = null, ariaCurrent: Boolean = false, content: @Composable () -> Unit) {
    val navigate = LocalNavigate.current
    A(href = path, attrs = {
        // className può contenere più classi separate da spazio: vanno passate come token distinti
        // (classList.add rifiuta una stringa con spazi).
        className?.let { cn -> classes(*cn.trim().split(Regex("\\s+")).toTypedArray()) }
        if (ariaCurrent) attr("aria-current", "page")
        onClick { ev -> ev.preventDefault(); navigate(path) }
    }) { content() }
}

@Composable
fun AppLink(path: String, label: String, className: String? = null) =
    AppLink(path, className) { Text(label) }
