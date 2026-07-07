package com.example.insurance.designsystem.style

import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.css.selectors.*

/**
 * Chunk PRODOTTI: card dominate dall'immagine con badge a pillola navy (un design moderno
 * "Prodotti in evidenza") e card prodotto con icona per la home.
 */
object ProductStyles : StyleSheet() {

    val card by style {
        property("display", "flex"); property("flex-direction", "column")
        property("background", "var(--surface)"); property("border", "1px solid var(--line)")
        property("border-radius", "var(--radius-lg)"); property("overflow", "hidden")
        property("transition", "box-shadow .15s, transform .15s")
        self + hover style { property("box-shadow", "var(--shadow)"); property("transform", "translateY(-3px)") }
    }
    /** Media: gradiente navy con icona (placeholder dell'immagine prodotto). */
    val media by style {
        property("position", "relative"); property("aspect-ratio", "16 / 10")
        property("background", "linear-gradient(135deg, var(--brand-blue), var(--brand-blue-700))")
        property("display", "grid"); property("place-items", "center")
    }
    val mediaIcon by style { property("font-size", "56px"); property("opacity", ".95") }
    /** Badge categoria: pillola navy in overlay (PER LA TUA CASA…). */
    val badge by style {
        property("position", "absolute"); property("left", "var(--sp-3)"); property("top", "var(--sp-3)")
        property("background", "var(--brand-blue)"); property("color", "#fff"); property("font-size", "var(--fs-xs)")
        property("font-weight", "700"); property("text-transform", "uppercase"); property("letter-spacing", ".06em")
        property("padding", "6px 12px"); property("border-radius", "var(--radius-pill)")
    }
    val body by style {
        property("padding", "var(--sp-5)"); property("display", "flex"); property("flex-direction", "column")
        property("gap", "var(--sp-2)"); property("flex", "1")
    }
    val titleLink by style {
        property("font-family", "var(--font-head)"); property("font-size", "1.35rem"); property("font-weight", "700")
        property("color", "var(--ink)")
        self + hover style { property("color", "var(--brand-blue)"); property("text-decoration", "none") }
    }
    val tagline by style { property("color", "var(--slate)"); property("flex", "1") }
}
