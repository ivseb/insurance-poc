package com.example.insurance.designsystem.style

import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.css.selectors.*

/**
 * Chunk HERO: hero istituzionale come un design moderno (immagine a tutta larghezza + card scura
 * in overlay, titolo serif), e hero compatto navy per le pagine prodotto.
 */
object HeroStyles : StyleSheet() {

    /** Hero a tutta immagine. Senza foto licenziata ACME: gradiente caldo evocativo. */
    val hero by style {
        property("position", "relative")
        property("min-height", "520px")
        property("display", "flex")
        property("align-items", "center")
        property("background", "linear-gradient(115deg, #2B1A12 0%, #6E3B22 38%, #B5683A 70%, #E0A06A 100%)")
        property("overflow", "hidden")
        // bottoni sull'hero scuro: primario pill bianco (testo navy), secondario outline bianco (un design moderno)
        property("--wa-color-brand-fill-loud", "#ffffff")
        property("--wa-color-brand-on-loud", "var(--brand-blue)")
        property("--wa-color-brand-border-loud", "#ffffff")
        property("--wa-color-brand-on-quiet", "#ffffff")
        property("--wa-color-brand-border-normal", "#ffffff")
        media(mediaMaxWidth(760.px)) { self style { property("min-height", "420px") } }
    }
    val heroInner by style {
        property("position", "relative"); property("z-index", "1"); property("width", "100%")
    }
    /** Card scura translucida in overlay (come un design moderno). */
    val card by style {
        property("max-width", "560px")
        property("background", "rgba(20,16,24,.55)")
        property("backdrop-filter", "blur(2px)")
        property("border-radius", "var(--radius-lg)")
        property("padding", "var(--sp-7)")
        property("color", "#fff")
        media(mediaMaxWidth(600.px)) { self style { property("padding", "var(--sp-5)") } }
    }
    val eyebrow by style {
        property("display", "inline-block"); property("color", "#fff"); property("font-weight", "600")
        property("text-decoration", "underline"); property("text-underline-offset", "4px"); property("margin-bottom", "var(--sp-3)")
    }
    val title by style {
        property("font-size", "var(--fs-hero)"); property("color", "#fff"); property("margin-bottom", "var(--sp-4)"); property("max-width", "14ch")
    }
    val sub by style {
        property("font-size", "1.2rem"); property("color", "rgba(255,255,255,.94)"); property("max-width", "46ch"); property("margin-bottom", "var(--sp-5)")
    }
    val actions by style { property("display", "flex"); property("gap", "var(--sp-3)"); property("flex-wrap", "wrap") }

    /** Hero compatto navy per /prodotto. */
    val productHero by style {
        property("background", "var(--brand-blue)"); property("color", "#fff"); property("padding-block", "var(--sp-8)")
        property("--wa-color-brand-fill-loud", "#ffffff"); property("--wa-color-brand-on-loud", "var(--brand-blue)")
        property("--wa-color-brand-border-loud", "#ffffff"); property("--wa-color-brand-on-quiet", "#ffffff")
        property("--wa-color-brand-border-normal", "#ffffff")
    }
    val productHeroTitle by style { property("font-size", "var(--fs-hero)"); property("color", "#fff"); property("max-width", "16ch") }
    val productHeroSub by style { property("font-size", "1.15rem"); property("color", "rgba(255,255,255,.94)"); property("max-width", "52ch") }
    val productHeroIcon by style { property("font-size", "56px"); property("margin-bottom", "var(--sp-3)") }
}
