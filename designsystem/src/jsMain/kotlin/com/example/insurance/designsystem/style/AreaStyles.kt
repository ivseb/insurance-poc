package com.example.insurance.designsystem.style

import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.css.selectors.*

/**
 * Chunk AREA PERSONALE: banda azioni verde, card agenzia, card polizza con badge
 * "copertura attiva" e nome come link serif, garanzie, form/login. Stile area clienti.
 */
object AreaStyles : StyleSheet() {

    val page by style { property("padding-block", "var(--sp-7) var(--sp-9)") }
    val pageTitle by style { property("margin-bottom", "var(--sp-6)") }

    // ---- Banda "Di cosa hai bisogno?" (verde tenue) ----
    val actionBand by style {
        property("background", "var(--green-050)"); property("border-radius", "var(--radius-lg)")
        property("padding", "var(--sp-6)"); property("margin-bottom", "var(--sp-7)")
    }
    val actionRow by style {
        property("display", "grid"); property("grid-template-columns", "1fr 1fr"); property("gap", "var(--sp-4)")
        media(mediaMaxWidth(600.px)) { self style { property("grid-template-columns", "1fr") } }
    }
    /** CTA verde a pillola con icona in cerchio + chevron. */
    val actionBtn by style {
        property("display", "flex"); property("align-items", "center"); property("gap", "var(--sp-4)")
        property("background", "var(--green-400)"); property("color", "var(--brand-blue)"); property("font-weight", "700")
        property("border-radius", "var(--radius-pill)"); property("padding", "var(--sp-3) var(--sp-5)"); property("min-height", "64px")
        property("border", "0"); property("cursor", "pointer"); property("font", "inherit"); property("font-weight", "700"); property("text-align", "left")
        self + hover style { property("filter", "brightness(.97)"); property("text-decoration", "none") }
    }
    val actionIcon by style {
        property("width", "44px"); property("height", "44px"); property("border-radius", "50%"); property("background", "#fff")
        property("display", "grid"); property("place-items", "center"); property("font-size", "20px"); property("flex", "0 0 auto")
    }
    val actionChevron by style { property("margin-left", "auto"); property("font-size", "20px") }

    // ---- Card agenzia ----
    val agencyCard by style {
        property("background", "var(--surface)"); property("border", "1px solid var(--line)"); property("border-radius", "var(--radius-lg)")
        property("padding", "var(--sp-5)"); property("box-shadow", "var(--shadow-sm)")
    }
    val agencyIcons by style { property("display", "flex"); property("gap", "var(--sp-3)"); property("margin-top", "var(--sp-4)") }
    val iconBtn by style {
        property("width", "46px"); property("height", "46px"); property("border-radius", "50%"); property("border", "1px solid var(--brand-blue)")
        property("color", "var(--brand-blue)"); property("display", "grid"); property("place-items", "center")
        self + hover style { property("background", "var(--brand-blue-050)"); property("text-decoration", "none") }
    }

    // ---- Card polizza (verde tenue, come areaclienti) ----
    val policyList by style { property("list-style", "none"); property("padding", "0"); property("margin", "0"); property("display", "grid"); property("gap", "var(--sp-5)") }
    val policyCard by style {
        property("background", "var(--green-050)"); property("border", "1px solid #CDE6D5"); property("border-radius", "var(--radius-lg)"); property("overflow", "hidden")
    }
    val policyHead by style {
        property("display", "flex"); property("align-items", "center"); property("gap", "var(--sp-4)"); property("flex-wrap", "wrap")
        property("padding", "var(--sp-4) var(--sp-5)")
    }
    val policyIcon by style {
        property("width", "48px"); property("height", "48px"); property("border-radius", "50%"); property("background", "#fff"); property("border", "1px solid #CDE6D5")
        property("display", "grid"); property("place-items", "center"); property("font-size", "22px"); property("flex", "0 0 auto")
    }
    val policySub by style { property("color", "var(--slate)"); property("font-size", "var(--fs-sm)") }
    val policyName by style { property("font-weight", "700"); property("color", "var(--ink)") }
    val policySpacer by style { property("flex", "1") }
    val policyNumLabel by style { property("font-size", "var(--fs-xs)"); property("text-transform", "uppercase"); property("letter-spacing", ".06em"); property("color", "var(--slate)") }
    /** Badge "copertura attiva": verde con spunta. */
    val statusBadge by style {
        property("display", "inline-flex"); property("align-items", "center"); property("gap", "var(--sp-1)")
        property("font-weight", "700"); property("font-size", "var(--fs-sm)"); property("text-transform", "uppercase"); property("letter-spacing", ".04em")
    }
    val policyBody by style { property("background", "var(--surface)"); property("padding", "var(--sp-5)") }
    /** Nome veicolo/polizza come link serif sottolineato navy (firma areaclienti). */
    val vehicleLink by style {
        property("font-family", "var(--font-head)"); property("font-size", "1.6rem"); property("font-weight", "700")
        property("color", "var(--brand-blue)"); property("text-decoration", "underline"); property("text-underline-offset", "3px")
        self + hover style { property("color", "var(--brand-blue-700)") }
    }
    val metaRow by style {
        property("display", "grid"); property("grid-template-columns", "repeat(4, 1fr)"); property("gap", "var(--sp-4)"); property("margin-top", "var(--sp-4)")
        media(mediaMaxWidth(600.px)) { self style { property("grid-template-columns", "1fr 1fr") } }
    }
    val metaLabel by style { property("font-size", "var(--fs-xs)"); property("text-transform", "uppercase"); property("letter-spacing", ".06em"); property("color", "var(--slate)"); property("margin-bottom", "2px") }
    val metaValue by style { property("font-weight", "600") }

    val coverages by style { property("margin", "var(--sp-2) 0 0"); property("padding-left", "var(--sp-5)") }

    // ---- Form / login ----
    val authCard by style {
        property("max-width", "440px"); property("margin-inline", "auto"); property("background", "var(--surface)")
        property("border", "1px solid var(--line)"); property("border-radius", "var(--radius-lg)"); property("padding", "var(--sp-6)"); property("box-shadow", "var(--shadow-sm)")
    }
    val stack by style { property("display", "grid"); property("gap", "var(--sp-3)") }
    val statusOk by style { property("color", "var(--green)"); property("font-weight", "600") }
}
