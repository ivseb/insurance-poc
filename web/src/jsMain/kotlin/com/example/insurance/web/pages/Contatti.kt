package com.example.insurance.web.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.example.insurance.designsystem.style.LayoutStyles
import com.example.insurance.designsystem.ui.*
import com.example.insurance.web.Routes
import com.example.insurance.web.ui.PageShell
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import org.jetbrains.compose.web.dom.*

@Page("/contatti")
@Composable
fun ContattiPage() {
    val ctx = rememberPageContext()
    val navigate: (String) -> Unit = { ctx.router.navigateTo(it) }

    CompositionLocalProvider(LocalNavigate provides navigate) {
        PageShell(navigate) {
            PageSection {
                SectionHead("Contatti", "Siamo qui per aiutarti", "Scegli il canale che preferisci: ti rispondiamo in fretta.")
                Grid(cols = 3) {
                    ContactCard("Telefono", "Lun–Ven 8:00–20:00, Sab 9:00–13:00", "800 123 456", "tel:800123456")
                    ContactCard("Email", "Risposta entro 24 ore lavorative", "supporto@acme-poc.example", "mailto:supporto@acme-poc.example")
                    ContactCard("Sinistri", "Numero verde attivo 24/7", "800 999 000", "tel:800999000")
                }
            }
            PageSection(alt = true) {
                Div(attrs = { classes(LayoutStyles.head) }) {
                    H2 { Text("Preferisci di persona?") }
                    P(attrs = { classes("muted") }) { Text("Trova l'agenzia ACME più vicina a te.") }
                    AppLink(Routes.TROVA_AGENZIA, className = LayoutStyles.linkArrow) { Text("Trova agenzia") }
                }
            }
        }
    }
}

@Composable
private fun ContactCard(title: String, sub: String, value: String, href: String) {
    Article(attrs = { classes(LayoutStyles.card) }) {
        H3 { Text(title) }
        P(attrs = { classes("muted") }) { Text(sub) }
        A(href = href, attrs = { classes(LayoutStyles.linkArrow) }) { Text(value) }
    }
}
