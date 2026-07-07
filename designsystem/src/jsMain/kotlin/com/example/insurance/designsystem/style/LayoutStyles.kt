package com.example.insurance.designsystem.style

import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.css.selectors.*

/**
 * Chunk LAYOUT: sezioni, intestazioni, griglie, card generiche, banda CTA.
 * StyleSheet tipizzato: i nomi-classe sono generati e referenziati via `LayoutStyles.x`.
 * Montato dai gusci di pagina → entra nell'HTML solo dove serve.
 */
object LayoutStyles : StyleSheet() {

    val section by style {
        property("padding-block", "var(--sp-9)")
    }
    val sectionAlt by style {
        property("padding-block", "var(--sp-9)")
        property("background", "var(--surface)")
    }
    val sectionWarm by style {
        property("padding-block", "var(--sp-9)")
        property("background", "var(--bg-warm)")
    }

    /** Intestazione sezione: eyebrow rosso + titolo serif. Centrata come un sito di riferimento. */
    val head by style {
        property("max-width", "62ch")
        property("margin", "0 auto var(--sp-7)")
        property("text-align", "center")
    }
    val headLeft by style {
        property("max-width", "62ch")
        property("margin-bottom", "var(--sp-7)")
    }
    val eyebrow by style {
        property("color", "var(--brand-red)")
        property("font-weight", "700")
        property("text-transform", "uppercase")
        property("letter-spacing", ".12em")
        property("font-size", "var(--fs-sm)")
        property("margin-bottom", "var(--sp-2)")
    }

    val grid by style {
        property("display", "grid")
        property("gap", "var(--sp-5)")
    }
    val cols2 by style {
        property("grid-template-columns", "repeat(2, 1fr)")
        media(mediaMaxWidth(600.px)) { self style { property("grid-template-columns", "1fr") } }
    }
    val cols3 by style {
        property("grid-template-columns", "repeat(3, 1fr)")
        media(mediaMaxWidth(920.px)) { self style { property("grid-template-columns", "1fr 1fr") } }
        media(mediaMaxWidth(600.px)) { self style { property("grid-template-columns", "1fr") } }
    }
    val cols4 by style {
        property("grid-template-columns", "repeat(4, 1fr)")
        media(mediaMaxWidth(920.px)) { self style { property("grid-template-columns", "1fr 1fr") } }
        media(mediaMaxWidth(600.px)) { self style { property("grid-template-columns", "1fr") } }
    }

    /** Card neutra con bordo morbido e hover sollevato (un sito di riferimento). */
    val card by style {
        property("background", "var(--surface)")
        property("border", "1px solid var(--line)")
        property("border-radius", "var(--radius-lg)")
        property("padding", "var(--sp-5)")
        property("box-shadow", "var(--shadow-sm)")
    }

    val feature by style {
        property("text-align", "left")
    }
    val featureIcon by style {
        property("width", "48px")
        property("height", "48px")
        property("border-radius", "50%")
        property("background", "var(--brand-blue-050)")
        property("color", "var(--brand-blue)")
        property("display", "grid")
        property("place-items", "center")
        property("font-size", "22px")
        property("font-weight", "800")
        property("margin-bottom", "var(--sp-3)")
    }

    /** Banda CTA a piè di sezione. */
    val ctaBand by style {
        property("background", "var(--brand-blue)")
        property("color", "#fff")
        property("border-radius", "var(--radius-lg)")
        property("margin", "var(--sp-8) auto")
        property("max-width", "var(--container)")
        // su navy i wa-button accent diventano bianchi
        property("--wa-color-brand-fill-loud", "#ffffff")
        property("--wa-color-brand-on-loud", "var(--brand-blue)")
        property("--wa-color-brand-border-loud", "#ffffff")
    }
    val ctaInner by style {
        property("display", "flex")
        property("align-items", "center")
        property("justify-content", "space-between")
        property("gap", "var(--sp-5)")
        property("padding", "var(--sp-7)")
        property("flex-wrap", "wrap")
        media(mediaMaxWidth(600.px)) { self style { property("flex-direction", "column"); property("align-items", "flex-start") } }
    }

    /** Link con freccia (→) animata. */
    val linkArrow by style {
        property("display", "inline-flex")
        property("align-items", "center")
        property("gap", "var(--sp-2)")
        property("font-weight", "700")
        property("color", "var(--brand-blue)")
        self + hover style { property("text-decoration", "none") }
        self + after style {
            property("content", "\"→\"")
            property("transition", "transform .15s")
        }
        self + hover + after style { property("transform", "translateX(3px)") }
    }
}
