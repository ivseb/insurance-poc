package com.example.insurance.web.pages.area

import androidx.compose.runtime.*
import com.example.insurance.designsystem.style.AreaStyles
import com.example.insurance.designsystem.style.LayoutStyles
import com.example.insurance.designsystem.ui.AppLink
import com.example.insurance.designsystem.ui.Grid
import com.example.insurance.designsystem.ui.LocalNavigate
import com.example.insurance.designsystem.wa.WaCallout
import com.example.insurance.shared.model.PolicyStatus
import com.example.insurance.shared.model.PolicySummary
import com.example.insurance.web.Bff
import com.example.insurance.web.Routes
import com.example.insurance.web.ui.AppShell
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import org.jetbrains.compose.web.dom.*

fun productIcon(product: String): String = when (product.lowercase()) {
    "auto" -> "🚗"; "casa" -> "🏠"; "vita" -> "💙"; else -> "📄"
}

@Page
@Composable
fun PoliciesPage() {
    val ctx = rememberPageContext()
    val navigate: (String) -> Unit = { ctx.router.navigateTo(it) }
    var policies by remember { mutableStateOf<List<PolicySummary>?>(null) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        runCatching { Bff.policies() }
            .onSuccess { policies = it }
            .onFailure { if (it is Bff.Unauthorized) navigate(Routes.LOGIN) else error = it.message ?: it.toString() }
    }

    CompositionLocalProvider(LocalNavigate provides navigate) {
        AppShell(navigate) {
            H1(attrs = { classes(AreaStyles.pageTitle) }) { Text("La tua area personale") }

            // --- NOVITÀ: 3 card editoriali in cima (come areaclienti) ---
            Grid(cols = 3) {
                NovitaCard("Convenzioni e vantaggi", "Sconti e servizi dedicati per prenderti cura di te.", "Scopri", Routes.PRODOTTI, navigate)
                NovitaCard("Chiedi a un esperto", "Una domanda a un professionista? Ti rispondiamo noi.", "Scopri", Routes.CONTATTI, navigate)
                NovitaCard("Dal nostro blog", "Approfondimenti e guide per scelte più consapevoli.", "Leggi", Routes.BLOG, navigate)
            }

            // --- Dashboard: "Di cosa hai bisogno?" + "La mia Agenzia" (come areaclienti) ---
            val firstClaimRoute = policies?.firstOrNull()?.id?.let { Routes.policyDetail(it) } ?: Routes.AREA_POLICIES
            Grid(cols = 2) {
                Div {
                    H2 { Text("Di cosa hai bisogno?") }
                    Div(attrs = { classes(AreaStyles.actionBand) }) {
                        Div(attrs = { classes(AreaStyles.actionRow) }) {
                            ActionBtn("📝", "Apri un sinistro") { navigate(firstClaimRoute) }
                            ActionBtn("🗂️", "Gestisci i tuoi sinistri") { navigate(firstClaimRoute) }
                        }
                    }
                }
                MyAgencyCard()
            }

            // --- Le mie polizze ---
            H2(attrs = { style { property("margin-top", "var(--sp-7)") } }) { Text("Le mie polizze") }
            when {
                error != null -> WaCallout("danger") { Text("Impossibile caricare le polizze.") }
                policies == null -> P(attrs = { attr("aria-live", "polite") }) { Text("Caricamento…") }
                else -> Ul(attrs = { classes(AreaStyles.policyList) }) {
                    policies!!.forEach { p -> PolicyCard(p) }
                }
            }
        }
    }
}

@Composable
private fun NovitaCard(title: String, text: String, cta: String, route: String, navigate: (String) -> Unit) {
    Article(attrs = { classes(LayoutStyles.card) }) {
        P(attrs = { classes(LayoutStyles.eyebrow); style { property("margin", "0 0 4px") } }) { Text("Novità") }
        H3 { Text(title) }
        P(attrs = { classes("muted") }) { Text(text) }
        AppLink(route, className = LayoutStyles.linkArrow) { Text(cta) }
    }
}

@Composable
private fun ActionBtn(icon: String, label: String, onClick: () -> Unit) {
    A(href = "#", attrs = {
        classes(AreaStyles.actionBtn)
        onClick { ev -> ev.preventDefault(); onClick() }
    }) {
        Span(attrs = { classes(AreaStyles.actionIcon); attr("aria-hidden", "true") }) { Text(icon) }
        Span { Text(label) }
        Span(attrs = { classes(AreaStyles.actionChevron); attr("aria-hidden", "true") }) { Text("›") }
    }
}

@Composable
private fun MyAgencyCard() {
    Div(attrs = { classes(AreaStyles.agencyCard) }) {
        P(attrs = { classes(AreaStyles.policyNumLabel); style { property("margin", "0 0 4px") } }) { Text("La mia agenzia") }
        P(attrs = { classes(AreaStyles.policyName); style { property("margin", "0") } }) { Text("ACME Milano Centro") }
        P(attrs = { classes("muted"); style { property("margin", "4px 0 0") } }) { Text("Corso Buenos Aires 12, 20124 Milano") }
        P(attrs = { style { property("margin", "8px 0 0") } }) {
            Text("Tel: "); B { Text("+39 02 1234 5678") }
        }
        Div(attrs = { classes(AreaStyles.agencyIcons) }) {
            A(href = "tel:+390212345678", attrs = { classes(AreaStyles.iconBtn); attr("aria-label", "Chiama l'agenzia") }) { Text("📞") }
            A(href = "mailto:milano.centro@acme-poc.example", attrs = { classes(AreaStyles.iconBtn); attr("aria-label", "Scrivi all'agenzia") }) { Text("✉️") }
            AppLink(Routes.TROVA_AGENZIA, className = AreaStyles.iconBtn) { Text("📍") }
        }
    }
}

@Composable
private fun PolicyCard(p: PolicySummary) {
    Li(attrs = { classes(AreaStyles.policyCard) }) {
        Div(attrs = { classes(AreaStyles.policyHead) }) {
            Div(attrs = { classes(AreaStyles.policyIcon); attr("aria-hidden", "true") }) { Text(productIcon(p.product)) }
            Div {
                P(attrs = { classes(AreaStyles.policySub); style { property("margin", "0") } }) { Text("ACME Assicurazioni S.p.A.") }
                P(attrs = { classes(AreaStyles.policyName); style { property("margin", "0") } }) { Text("Protezione ${p.product}") }
            }
            Div(attrs = { classes(AreaStyles.policySpacer) }) {}
            Div {
                P(attrs = { classes(AreaStyles.policyNumLabel); style { property("margin", "0") } }) { Text("Polizza n°") }
                Span { Text(p.number) }
            }
            StatusBadge(p.status)
        }
        Div(attrs = { classes(AreaStyles.policyBody) }) {
            AppLink(Routes.policyDetail(p.id), className = AreaStyles.vehicleLink) { Text("${p.product} · ${p.number}") }
            Div(attrs = { style { property("margin-top", "var(--sp-3)") } }) {
                AppLink(Routes.policyDetail(p.id), className = LayoutStyles.linkArrow) { Text("Vedi dettaglio") }
            }
        }
    }
}

@Composable
fun StatusBadge(status: PolicyStatus) {
    val (color, label) = when (status) {
        PolicyStatus.ACTIVE -> "var(--green)" to "✓ Copertura attiva"
        PolicyStatus.EXPIRING -> "#B25E00" to "In scadenza"
        PolicyStatus.EXPIRED -> "var(--brand-red-700)" to "Scaduta"
    }
    Span(attrs = { classes(AreaStyles.statusBadge); style { property("color", color) } }) { Text(label) }
}
