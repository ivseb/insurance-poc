package com.example.insurance.web.pages

import androidx.compose.runtime.*
import com.example.insurance.designsystem.ui.*
import com.example.insurance.designsystem.wa.*
import com.example.insurance.web.Routes
import com.example.insurance.web.ui.AppShell
import com.example.insurance.web.ui.PageStyles
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import org.jetbrains.compose.web.css.Style
import org.jetbrains.compose.web.dom.*

/**
 * "Annotatore"/anteprima del design system: ogni componente mostrato in isolamento,
 * con titolo e descrizione. Equivale a uno Storybook per Compose HTML.
 */
@Page("/design-system")
@Composable
fun DesignSystemPage() {
    val ctx = rememberPageContext()
    val navigate: (String) -> Unit = { ctx.router.navigateTo(it) }

    CompositionLocalProvider(LocalNavigate provides navigate) {
        AppShell(navigate) {
            Style(PageStyles)
            H1 { Text("Design System") }
            P(attrs = { classes("muted") }) {
                Text("Anteprima dei componenti riusabili del modulo :designsystem (Compose HTML + Web Awesome).")
            }

            Showcase("Bottoni", "Web Awesome, tema ACME. Varianti accent/outlined, dimensioni.") {
                Div(attrs = { classes(PageStyles.dsRow) }) {
                    WaButton(appearance = "accent") { Text("Primario") }
                    WaButton(appearance = "outlined") { Text("Secondario") }
                    WaButton(appearance = "accent", size = "small") { Text("Small") }
                    WaButton(appearance = "accent", size = "large") { Text("Large") }
                }
            }

            Showcase("Badge di stato", "Mappati sugli stati polizza.") {
                Div(attrs = { classes(PageStyles.dsRow) }) {
                    WaBadge("success") { Text("Attiva") }
                    WaBadge("warning") { Text("In scadenza") }
                    WaBadge("danger") { Text("Scaduta") }
                }
            }

            Showcase("Callout", "Messaggi accessibili (role gestito da Web Awesome).") {
                Div(attrs = { classes(PageStyles.stack) }) {
                    WaCallout("success") { Text("Operazione completata.") }
                    WaCallout("danger") { Text("Si è verificato un errore.") }
                    WaCallout("neutral") { Text("Informazione di contesto.") }
                }
            }

            Showcase("Campi form", "Input e textarea accessibili con label e hint.") {
                Div(attrs = { classes(PageStyles.stack); style { property("max-width", "420px") } }) {
                    WaInput(label = "Nome utente", id = "ds-user") {}
                    WaInput(label = "Con hint/errore", id = "ds-hint", hint = "Campo obbligatorio") {}
                    WaTextarea(label = "Descrizione", id = "ds-desc", rows = 3) {}
                }
            }

            Showcase("Card prodotto", "Usata nella sezione istituzionale.") {
                Grid(cols = 3) {
                    ProductCard("🚗", "Auto", "RC, furto e assistenza stradale.", DsAction("Scopri") { navigate(Routes.PRODOTTI) })
                    ProductCard("🏠", "Casa", "Incendio, furto e danni.", DsAction("Scopri") { navigate(Routes.PRODOTTI) })
                }
            }

            Showcase("Feature", "Elenco 'perché'.") {
                Grid(cols = 3) {
                    FeatureItem(1, "Tutto online", "Gestisci polizze e sinistri 24/7.")
                    FeatureItem(2, "Assistenza vera", "Agenti sul territorio.")
                }
            }

            Showcase("Section head", "Intestazione di sezione con eyebrow.") {
                SectionHead("Eyebrow", "Titolo di sezione", "Sottotitolo descrittivo opzionale.")
            }
        }
    }
}

@Composable
private fun Showcase(title: String, description: String, content: @Composable () -> Unit) {
    Div(attrs = { classes(PageStyles.dsItem) }) {
        H2 { Text(title) }
        P(attrs = { classes("muted") }) { Text(description) }
        Div(attrs = { classes(PageStyles.dsCanvas) }) { content() }
    }
}
