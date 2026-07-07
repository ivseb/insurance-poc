package com.example.insurance.designsystem.ui

import androidx.compose.runtime.Composable
import com.example.insurance.designsystem.style.HeroStyles
import com.example.insurance.designsystem.style.LayoutStyles
import com.example.insurance.designsystem.style.ProductStyles
import com.example.insurance.designsystem.wa.WaButton
import org.jetbrains.compose.web.css.Style
import org.jetbrains.compose.web.dom.*

/** Azione (etichetta + callback) per le CTA dei componenti. */
class DsAction(val label: String, val onClick: () -> Unit)

/**
 * Hero istituzionale stile moderno: immagine a tutta larghezza + card scura in overlay,
 * titolo serif e bottoni a pillola. Presentazionale: riceve testi + CTA.
 * Monta il proprio chunk di stile (HeroStyles) → caricato solo dalle pagine che usano l'hero.
 */
@Composable
fun Hero(eyebrow: String, title: String, subtitle: String, primary: DsAction, secondary: DsAction? = null) {
    Style(HeroStyles)
    Section(attrs = { classes(HeroStyles.hero) }) {
        Div(attrs = { classes("container", HeroStyles.heroInner) }) {
            Div(attrs = { classes(HeroStyles.card) }) {
                P(attrs = { classes(HeroStyles.eyebrow) }) { Text(eyebrow) }
                H1(attrs = { classes(HeroStyles.title) }) { Text(title) }
                P(attrs = { classes(HeroStyles.sub) }) { Text(subtitle) }
                Div(attrs = { classes(HeroStyles.actions) }) {
                    WaButton(variant = "brand", appearance = "accent", pill = true, onClick = primary.onClick) { Text(primary.label) }
                    secondary?.let { s ->
                        WaButton(variant = "brand", appearance = "outlined", pill = true, onClick = s.onClick) { Text(s.label) }
                    }
                }
            }
        }
    }
}

/** Hero compatto navy per le pagine prodotto. */
@Composable
fun ProductHero(icon: String, claim: String, subtitle: String, primary: DsAction, secondary: DsAction? = null) {
    Style(HeroStyles)
    Section(attrs = { classes(HeroStyles.productHero) }) {
        Div(attrs = { classes("container") }) {
            Div(attrs = { classes(HeroStyles.productHeroIcon); attr("aria-hidden", "true") }) { Text(icon) }
            H1(attrs = { classes(HeroStyles.productHeroTitle) }) { Text(claim) }
            P(attrs = { classes(HeroStyles.productHeroSub) }) { Text(subtitle) }
            Div(attrs = { classes(HeroStyles.actions); style { property("margin-top", "var(--sp-5)") } }) {
                WaButton(variant = "brand", appearance = "accent", pill = true, onClick = primary.onClick) { Text(primary.label) }
                secondary?.let { s ->
                    WaButton(variant = "brand", appearance = "outlined", pill = true, onClick = s.onClick) { Text(s.label) }
                }
            }
        }
    }
}

/**
 * Card prodotto dominata dall'immagine ("Prodotti in evidenza"): media + badge
 * pillola navy opzionale, titolo serif cliccabile, link freccia.
 */
@Composable
fun ProductCard(icon: String, name: String, tagline: String, action: DsAction, badge: String? = null) {
    Style(ProductStyles)
    Article(attrs = { classes(ProductStyles.card) }) {
        Div(attrs = { classes(ProductStyles.media); attr("aria-hidden", "true") }) {
            badge?.let { Span(attrs = { classes(ProductStyles.badge) }) { Text(it) } }
            Span(attrs = { classes(ProductStyles.mediaIcon) }) { Text(icon) }
        }
        Div(attrs = { classes(ProductStyles.body) }) {
            A(href = "#", attrs = {
                classes(ProductStyles.titleLink)
                onClick { ev -> ev.preventDefault(); action.onClick() }
            }) { Text(name) }
            P(attrs = { classes(ProductStyles.tagline) }) { Text(tagline) }
            A(href = "#", attrs = {
                classes(LayoutStyles.linkArrow)
                onClick { ev -> ev.preventDefault(); action.onClick() }
            }) { Text(action.label) }
        }
    }
}

/** Voce "perché ACME" con numero/icona. */
@Composable
fun FeatureItem(index: Int, title: String, text: String) {
    Div(attrs = { classes(LayoutStyles.feature) }) {
        Div(attrs = { classes(LayoutStyles.featureIcon); attr("aria-hidden", "true") }) { Text("$index") }
        H3 { Text(title) }
        P(attrs = { classes("muted") }) { Text(text) }
    }
}
