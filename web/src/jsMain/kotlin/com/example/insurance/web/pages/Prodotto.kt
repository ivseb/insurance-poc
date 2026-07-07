package com.example.insurance.web.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.example.insurance.designsystem.style.LayoutStyles
import com.example.insurance.designsystem.ui.*
import com.example.insurance.designsystem.wa.WaButton
import com.example.insurance.designsystem.wa.WaCallout
import com.example.insurance.web.Routes
import com.example.insurance.web.content.SiteContent
import com.example.insurance.web.ui.PageShell
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import org.jetbrains.compose.web.dom.*

@Page("/prodotto")
@Composable
fun ProdottoPage() {
    val ctx = rememberPageContext()
    val navigate: (String) -> Unit = { ctx.router.navigateTo(it) }
    val product = ctx.route.params["key"]?.let { SiteContent.products[it] }

    CompositionLocalProvider(LocalNavigate provides navigate) {
        PageShell(navigate) {
            if (product == null) {
                PageSection {
                    WaCallout("danger") { Text("Prodotto non trovato.") }
                    Div(attrs = { style { property("margin-top", "16px") } }) {
                        AppLink(Routes.PRODOTTI, className = LayoutStyles.linkArrow) { Text("Tutti i prodotti") }
                    }
                }
            } else {
                ProductHero(
                    icon = product.icon,
                    claim = product.claim,
                    subtitle = product.intro,
                    primary = DsAction("Richiedi preventivo") { navigate(Routes.LOGIN) },
                    secondary = DsAction("Trova agenzia") { navigate(Routes.TROVA_AGENZIA) },
                )
                PageSection {
                    SectionHead("Coperture", "Cosa include la polizza ${product.name}")
                    Grid(cols = 2) {
                        product.coverages.forEach { (titolo, descr) ->
                            Article(attrs = { classes(LayoutStyles.card) }) {
                                H3 { Text(titolo) }
                                P(attrs = { classes("muted") }) { Text(descr) }
                            }
                        }
                    }
                }
                Div(attrs = { classes(LayoutStyles.ctaBand) }) {
                    Div(attrs = { classes(LayoutStyles.ctaInner) }) {
                        H2(attrs = { style { property("color", "#fff"); property("margin", "0") } }) {
                            Text("Pronto a proteggere la tua ${product.name.lowercase()}?")
                        }
                        WaButton(variant = "brand", appearance = "accent", pill = true, size = "large", onClick = { navigate(Routes.LOGIN) }) {
                            Text("Richiedi preventivo")
                        }
                    }
                }
            }
        }
    }
}
