package com.example.insurance.web.ui

import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.css.selectors.*

/**
 * Chunk di stile delle pagine editoriali/utility del sito (blog, legali, store locator,
 * gallery design system). Montato solo dalle pagine che lo usano → caricamento per route.
 */
object PageStyles : StyleSheet() {

    // Blog
    val articles by style { property("display", "grid"); property("gap", "var(--sp-5)"); property("max-width", "760px") }
    val post by style { property("padding", "var(--sp-6)") }
    val postMeta by style { property("display", "flex"); property("gap", "var(--sp-3)"); property("align-items", "center"); property("margin-bottom", "var(--sp-2)") }

    // Pagine legali
    val legal by style { property("max-width", "760px") }

    // Store locator
    val locatorSearch by style { property("max-width", "420px"); property("margin-bottom", "var(--sp-6)") }
    val agency by style {
        property("background", "var(--surface)"); property("border", "1px solid var(--line)"); property("border-radius", "var(--radius-lg)")
        property("padding", "var(--sp-5)"); property("box-shadow", "var(--shadow-sm)")
    }

    // Stack generico
    val stack by style { property("display", "grid"); property("gap", "var(--sp-3)") }

    // Gallery design system
    val dsItem by style { property("padding", "var(--sp-5) 0"); property("border-bottom", "1px solid var(--line)") }
    val dsCanvas by style { property("margin-top", "var(--sp-4)"); property("padding", "var(--sp-5)"); property("background", "var(--bg)"); property("border-radius", "var(--radius-lg)") }
    val dsRow by style { property("display", "flex"); property("gap", "var(--sp-3)"); property("flex-wrap", "wrap"); property("align-items", "center") }
}
