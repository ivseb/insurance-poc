package com.example.insurance.designsystem.style

import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.css.selectors.*

/**
 * Chunk CHROME: header istituzionale (top-bar segmentata + masthead flottante, stile moderno),
 * header area personale (barra bianca con underline rosso attivo, stile areaclienti) e footer.
 */
object ChromeStyles : StyleSheet() {

    // ---- Top utility bar (istituzionale) ----
    val topbar by style { property("background", "var(--surface)"); property("border-bottom", "1px solid var(--line)"); property("font-size", "var(--fs-sm)") }
    val topInner by style {
        property("display", "flex"); property("align-items", "center"); property("gap", "var(--sp-4)"); property("height", "44px")
        media(mediaMaxWidth(760.px)) { self style { property("overflow-x", "auto") } }
    }
    val seg by style {
        property("display", "inline-flex"); property("align-items", "center"); property("gap", "var(--sp-2)")
        property("padding", "var(--sp-1)"); property("background", "var(--bg)"); property("border-radius", "var(--radius-pill)")
    }
    val segBtn by style {
        property("padding", "6px 16px"); property("border-radius", "var(--radius-pill)"); property("font-weight", "600")
        property("color", "var(--ink)"); property("white-space", "nowrap")
        self + hover style { property("text-decoration", "none") }
    }
    val segActive by style { property("background", "var(--brand-blue)"); property("color", "#fff") }
    val topSpacer by style { property("flex", "1") }
    val topLink by style { property("color", "var(--ink)"); property("font-weight", "600"); property("white-space", "nowrap") }
    val meteo by style {
        property("display", "inline-flex"); property("align-items", "center"); property("gap", "var(--sp-1)")
        property("padding", "4px 14px"); property("border", "1px solid var(--brand-red)"); property("color", "var(--brand-red)")
        property("border-radius", "var(--radius-pill)"); property("font-weight", "700")
    }

    // ---- Masthead flottante (istituzionale) ----
    val masthead by style { property("position", "sticky"); property("top", "0"); property("z-index", "40"); property("background", "transparent"); property("padding-top", "var(--sp-3)") }
    val mastheadInner by style {
        property("display", "flex"); property("align-items", "center"); property("gap", "var(--sp-6)"); property("height", "68px")
        property("background", "var(--surface)"); property("border-radius", "var(--radius-pill)")
        property("box-shadow", "var(--shadow)"); property("padding", "0 var(--sp-5)"); property("position", "relative")
        media(mediaMaxWidth(760.px)) { self style { property("gap", "var(--sp-2)"); property("border-radius", "var(--radius-lg)") } }
    }

    // ---- Brand (logo ACME-like: box navy con barra rossa) ----
    val brand by style { property("display", "inline-flex"); property("align-items", "center"); property("gap", "var(--sp-3)") }
    val brandMark by style {
        property("display", "grid"); property("place-items", "center"); property("width", "46px"); property("height", "40px")
        property("background", "var(--brand-blue)"); property("color", "#fff"); property("font-weight", "800"); property("font-size", "13px")
        property("letter-spacing", ".02em"); property("position", "relative"); property("overflow", "hidden")
        self + after style {
            property("content", "\"\""); property("position", "absolute"); property("right", "0"); property("bottom", "0")
            property("width", "120%"); property("height", "8px"); property("background", "var(--brand-red)"); property("transform", "rotate(-18deg) translateY(4px)")
        }
    }
    val brandName by style { property("font-weight", "700"); property("color", "var(--ink)"); property("letter-spacing", ".01em") }

    // ---- Nav principale ----
    val mainnav by style {
        property("display", "flex"); property("gap", "var(--sp-5)")
        media(mediaMaxWidth(760.px)) { self style { property("display", "none") } }
    }
    val mainnavOpen by style {
        property("display", "flex"); property("flex-direction", "column"); property("gap", "0")
        property("position", "absolute"); property("top", "76px"); property("left", "0"); property("right", "0"); property("z-index", "39")
        property("background", "var(--surface)"); property("border-radius", "var(--radius-lg)"); property("box-shadow", "var(--shadow)")
        property("padding", "var(--sp-2) var(--sp-5)")
    }
    val navLink by style {
        property("color", "var(--ink)"); property("font-weight", "600"); property("padding", "var(--sp-2) 0")
        property("border-bottom", "3px solid transparent")
        self + hover style { property("color", "var(--brand-blue)"); property("border-bottom-color", "var(--brand-red)"); property("text-decoration", "none") }
    }
    val navLinkActive by style { property("color", "var(--brand-blue)"); property("border-bottom-color", "var(--brand-red)") }
    /** Etichetta statica di contesto nella top-bar (non è un toggle). */
    val topContext by style { property("color", "var(--slate)"); property("font-weight", "600") }
    val spacer by style { property("flex", "1") }
    val navToggle by style {
        property("display", "none"); property("width", "44px"); property("height", "44px"); property("align-items", "center"); property("justify-content", "center")
        property("font-size", "22px"); property("background", "none"); property("border", "0"); property("cursor", "pointer"); property("color", "var(--ink)")
        media(mediaMaxWidth(760.px)) { self style { property("display", "inline-flex"); property("order", "3"); property("margin-left", "auto") } }
    }

    // ---- Header AREA PERSONALE (barra bianca, underline rosso attivo) ----
    val appbar by style {
        property("position", "sticky"); property("top", "0"); property("z-index", "40"); property("background", "var(--surface)"); property("border-bottom", "1px solid var(--line)")
    }
    val appbarInner by style { property("display", "flex"); property("align-items", "center"); property("gap", "var(--sp-6)"); property("height", "68px") }
    val appNav by style {
        property("display", "flex"); property("gap", "var(--sp-5)")
        media(mediaMaxWidth(600.px)) { self style { property("display", "none") } }
    }
    val appLink by style {
        property("color", "var(--ink)"); property("font-weight", "600"); property("padding", "22px 0"); property("border-bottom", "3px solid transparent"); property("text-transform", "uppercase"); property("font-size", "var(--fs-sm)"); property("letter-spacing", ".04em")
        self + hover style { property("color", "var(--brand-blue)"); property("text-decoration", "none") }
    }
    val appLinkActive by style { property("color", "var(--brand-blue)"); property("border-bottom-color", "var(--brand-red)") }

    // ---- Footer ----
    val footer by style { property("background", "var(--ink)"); property("color", "#C9C9D2"); property("padding-block", "var(--sp-8) var(--sp-6)"); property("font-size", "var(--fs-sm)"); property("margin-top", "var(--sp-9)") }
    val cols by style {
        property("display", "grid"); property("grid-template-columns", "1.4fr repeat(4, 1fr)"); property("gap", "var(--sp-6)")
        media(mediaMaxWidth(920.px)) { self style { property("grid-template-columns", "1fr 1fr") } }
        media(mediaMaxWidth(460.px)) { self style { property("grid-template-columns", "1fr") } }
    }
    val colTitle by style { property("color", "#fff"); property("font-family", "var(--font-body)"); property("font-size", "var(--fs-sm)"); property("text-transform", "uppercase"); property("letter-spacing", ".08em"); property("margin", "0 0 var(--sp-3)"); property("font-weight", "700") }
    val colList by style { property("list-style", "none"); property("margin", "0"); property("padding", "0"); property("display", "grid"); property("gap", "var(--sp-2)") }
    val footLink by style { property("color", "#C9C9D2"); self + hover style { property("color", "#fff") } }
    val legal by style {
        property("border-top", "1px solid #3A3A46"); property("margin-top", "var(--sp-6)"); property("padding-top", "var(--sp-4)")
        property("display", "flex"); property("gap", "var(--sp-5)"); property("flex-wrap", "wrap"); property("color", "#8A8A96")
    }
}
