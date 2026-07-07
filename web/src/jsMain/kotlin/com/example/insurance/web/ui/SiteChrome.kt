package com.example.insurance.web.ui

import androidx.compose.runtime.*
import com.example.insurance.designsystem.style.AreaStyles
import com.example.insurance.designsystem.style.ChromeStyles
import com.example.insurance.designsystem.style.LayoutStyles
import com.example.insurance.designsystem.ui.AppLink
import com.example.insurance.designsystem.wa.WaButton
import com.example.insurance.web.Routes
import kotlinx.browser.window
import org.jetbrains.compose.web.css.Style
import org.jetbrains.compose.web.dom.*

/** Logo ACME-like: box blu con barra rossa + wordmark. */
@Composable
fun BrandLogo() {
    AppLink(Routes.HOME, className = ChromeStyles.brand) {
        Span(attrs = { classes(ChromeStyles.brandMark); attr("aria-hidden", "true") }) { Text("ACME") }
        Span(attrs = { classes(ChromeStyles.brandName) }) { Text("Assicurazioni") }
    }
}

// ---------------------------------------------------------------------------
// Header ISTITUZIONALE (stile moderno): top-bar segmentata + masthead flottante.
// ---------------------------------------------------------------------------
/** Top utility bar: contesto a sinistra (statico, non un toggle) + link utili a destra. */
@Composable
private fun TopBar() {
    Div(attrs = { classes(ChromeStyles.topbar) }) {
        Div(attrs = { classes("container", ChromeStyles.topInner) }) {
            Span(attrs = { classes(ChromeStyles.topContext) }) { Text("Sito per i privati") }
            Div(attrs = { classes(ChromeStyles.topSpacer) }) {}
            AppLink(Routes.TROVA_AGENZIA, "Trova agenzia", className = ChromeStyles.topLink)
            AppLink(Routes.BLOG, "Blog", className = ChromeStyles.topLink)
            AppLink(Routes.CONTATTI, "Contatti", className = ChromeStyles.topLink)
        }
    }
}

/** Voci di nav = categorie prodotto + Chi siamo (come un design moderno), niente duplicato della Home. */
private val navItems = listOf(
    "Auto" to Routes.product("auto"),
    "Casa" to Routes.product("casa"),
    "Vita" to Routes.product("vita"),
    "Chi siamo" to Routes.CHI_SIAMO,
)

@Composable
private fun Masthead(navigate: (String) -> Unit) {
    var menuOpen by remember { mutableStateOf(false) }
    // Path corrente (la masthead ricompone a ogni cambio pagina) per evidenziare la voce attiva.
    val current = window.location.pathname + window.location.search
    Header(attrs = { classes(ChromeStyles.masthead) }) {
        Div(attrs = { classes("container") }) {
            Div(attrs = { classes(ChromeStyles.mastheadInner) }) {
                BrandLogo()
                Button(attrs = {
                    classes(ChromeStyles.navToggle)
                    attr("aria-label", "Apri menu")
                    attr("aria-expanded", menuOpen.toString())
                    attr("aria-controls", "mainnav")
                    onClick { menuOpen = !menuOpen }
                }) { Text(if (menuOpen) "✕" else "☰") }

                Nav(attrs = {
                    id("mainnav")
                    classes(if (menuOpen) ChromeStyles.mainnavOpen else ChromeStyles.mainnav)
                    attr("aria-label", "Principale")
                }) {
                    navItems.forEach { (label, route) ->
                        val active = current == route
                        val cls = if (active) "${ChromeStyles.navLink} ${ChromeStyles.navLinkActive}" else ChromeStyles.navLink
                        AppLink(route, label, className = cls)
                    }
                }
                Div(attrs = { classes(ChromeStyles.spacer) }) {}
                WaButton(variant = "brand", appearance = "accent", pill = true, onClick = { navigate(Routes.LOGIN) }) {
                    Text("👤 Area Clienti")
                }
            }
        }
    }
}

@Composable
fun SiteHeader(navigate: (String) -> Unit) {
    TopBar()
    Masthead(navigate)
}

// ---------------------------------------------------------------------------
// Header AREA PERSONALE (stile areaclienti): barra bianca, underline rosso attivo.
// ---------------------------------------------------------------------------
@Composable
fun AppHeader(navigate: (String) -> Unit) {
    Header(attrs = { classes(ChromeStyles.appbar) }) {
        Div(attrs = { classes("container", ChromeStyles.appbarInner) }) {
            BrandLogo()
            Nav(attrs = { classes(ChromeStyles.appNav); attr("aria-label", "Area personale") }) {
                AppLink(Routes.AREA_POLICIES, "Le mie polizze", className = "${ChromeStyles.appLink} ${ChromeStyles.appLinkActive}")
            }
            Div(attrs = { classes(ChromeStyles.spacer) }) {}
            WaButton(variant = "brand", appearance = "outlined", pill = true, onClick = { navigate(Routes.HOME) }) { Text("Esci") }
        }
    }
}

// ---------------------------------------------------------------------------
// Footer
// ---------------------------------------------------------------------------
@Composable
fun SiteFooter() {
    Footer(attrs = { classes(ChromeStyles.footer) }) {
        Div(attrs = { classes("container") }) {
            Div(attrs = { classes(ChromeStyles.cols) }) {
                Div {
                    BrandLogo()
                    P(attrs = { style { property("margin-top", "16px") } }) {
                        Text("Soluzioni assicurative per auto, casa e vita. POC dimostrativa.")
                    }
                }
                FooterCol("Prodotti", listOf(
                    "Auto" to Routes.product("auto"), "Casa" to Routes.product("casa"), "Vita" to Routes.product("vita"),
                ))
                FooterCol("Servizi", listOf(
                    "Area Clienti" to Routes.LOGIN, "Apri un sinistro" to Routes.LOGIN, "Le tue polizze" to Routes.AREA_POLICIES,
                ))
                FooterCol("Azienda", listOf(
                    "Chi siamo" to Routes.CHI_SIAMO, "Contatti" to Routes.CONTATTI, "Trova agenzia" to Routes.TROVA_AGENZIA,
                ))
                FooterCol("Risorse", listOf(
                    "Blog" to Routes.BLOG, "Design system" to Routes.DESIGN_SYSTEM, "Home" to Routes.HOME,
                ))
            }
            Div(attrs = { classes(ChromeStyles.legal) }) {
                Span { Text("© 2026 ACME Assicurazioni — POC") }
                AppLink(Routes.PRIVACY, "Privacy", className = ChromeStyles.footLink)
                AppLink(Routes.COOKIE, "Cookie", className = ChromeStyles.footLink)
                AppLink(Routes.ACCESSIBILITA, "Accessibilità", className = ChromeStyles.footLink)
            }
        }
    }
}

@Composable
private fun FooterCol(title: String, links: List<Pair<String, String>>) {
    Div {
        H4(attrs = { classes(ChromeStyles.colTitle) }) { Text(title) }
        Ul(attrs = { classes(ChromeStyles.colList) }) {
            links.forEach { (label, path) -> Li { AppLink(path, label, className = ChromeStyles.footLink) } }
        }
    }
}

/** Guscio pagine istituzionali: monta i chunk chrome+layout, poi header + <main> + footer. */
@Composable
fun PageShell(navigate: (String) -> Unit, main: @Composable () -> Unit) {
    Style(ChromeStyles)
    Style(LayoutStyles)
    A(href = "#main", attrs = { classes("skip-link") }) { Text("Salta al contenuto") }
    SiteHeader(navigate)
    Main(attrs = { id("main") }) { main() }
    SiteFooter()
}

/** Guscio area personale: header dedicato + <main> centrato (senza footer marketing). */
@Composable
fun AppShell(navigate: (String) -> Unit, main: @Composable () -> Unit) {
    Style(ChromeStyles)
    Style(LayoutStyles)
    Style(AreaStyles)
    A(href = "#main", attrs = { classes("skip-link") }) { Text("Salta al contenuto") }
    AppHeader(navigate)
    Main(attrs = { id("main"); classes("container", AreaStyles.page) }) { main() }
}
