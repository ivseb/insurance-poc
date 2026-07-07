package com.example.insurance.web.pages.area

import androidx.compose.runtime.*
import com.example.insurance.designsystem.style.AreaStyles
import com.example.insurance.designsystem.style.LayoutStyles
import com.example.insurance.shared.model.*
import com.example.insurance.shared.validation.ClaimValidation
import com.example.insurance.web.Bff
import com.example.insurance.web.Routes
import com.example.insurance.web.ui.AppShell
import com.example.insurance.designsystem.ui.LocalNavigate
import com.example.insurance.designsystem.wa.*
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.attributes.onSubmit
import org.jetbrains.compose.web.dom.*

@Page("/area/policy")
@Composable
fun PolicyPage() {
    val ctx = rememberPageContext()
    val navigate: (String) -> Unit = { ctx.router.navigateTo(it) }
    val id = ctx.route.params["id"]
    var detail by remember { mutableStateOf<PolicyDetail?>(null) }
    var error by remember { mutableStateOf(false) }

    LaunchedEffect(id) {
        if (id != null) runCatching { Bff.policy(id) }
            .onSuccess { detail = it }
            .onFailure { if (it is Bff.Unauthorized) navigate(Routes.LOGIN) else error = true }
    }

    CompositionLocalProvider(LocalNavigate provides navigate) {
        AppShell(navigate) {
            when {
                error -> WaCallout("danger") { Text("Polizza non trovata.") }
                detail == null -> P(attrs = { attr("aria-live", "polite") }) { Text("Caricamento dettaglio…") }
                else -> {
                    val d = detail!!
                    Div(attrs = { classes(AreaStyles.policyCard) }) {
                        Div(attrs = { classes(AreaStyles.policyHead) }) {
                            Div(attrs = { classes(AreaStyles.policyIcon); attr("aria-hidden", "true") }) { Text(productIcon(d.product)) }
                            Div {
                                P(attrs = { classes(AreaStyles.policySub); style { property("margin", "0") } }) { Text("Polizza n° ${d.number}") }
                                P(attrs = { classes(AreaStyles.policyName); style { property("margin", "0") } }) { Text("Protezione ${d.product}") }
                            }
                            Div(attrs = { classes(AreaStyles.policySpacer) }) {}
                            StatusBadge(d.status)
                        }
                        Div(attrs = { classes(AreaStyles.policyBody) }) {
                            H1(attrs = { classes(AreaStyles.vehicleLink); style { property("display", "inline-block"); property("text-decoration", "none") } }) { Text("${d.product} · ${d.number}") }
                            Div(attrs = { classes(AreaStyles.metaRow) }) {
                                Meta("Contraente", d.holder)
                                Meta("Validità", "${d.startDate} → ${d.endDate}")
                                Meta("Premio annuo", "€ ${d.premiumEuro}")
                                Meta("Stato", statusLabel(d.status))
                            }
                            H2(attrs = { style { property("margin-top", "var(--sp-5)") } }) { Text("Garanzie") }
                            Ul(attrs = { classes(AreaStyles.coverages) }) {
                                d.coverages.forEach { c -> Li { Text("${c.name} — massimale € ${c.ceilingEuro}") } }
                            }
                        }
                    }
                    ClaimForm(d.id)
                }
            }
        }
    }
}

private fun statusLabel(s: PolicyStatus) = when (s) {
    PolicyStatus.ACTIVE -> "Attiva"; PolicyStatus.EXPIRING -> "In scadenza"; PolicyStatus.EXPIRED -> "Scaduta"
}

@Composable
private fun Meta(label: String, value: String) {
    Div {
        P(attrs = { classes(AreaStyles.metaLabel); style { property("margin", "0") } }) { Text(label) }
        P(attrs = { classes(AreaStyles.metaValue); style { property("margin", "0") } }) { Text(value) }
    }
}

@Composable
private fun ClaimForm(policyId: String) {
    val scope = rememberCoroutineScope()
    var occurredOn by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var errors by remember { mutableStateOf<Map<String, String>>(emptyMap()) }
    var done by remember { mutableStateOf<ClaimResponse?>(null) }

    Div(attrs = { classes(LayoutStyles.card); style { property("margin-top", "var(--sp-5)") } }) {
        H2 { Text("Apri un sinistro") }
        done?.let { WaCallout("success") { Text("Sinistro ${it.claimId} ricevuto (${it.status})") } }

        Form(attrs = {
            onSubmit { ev ->
                ev.preventDefault()
                val req = ClaimRequest(policyId, occurredOn, description)
                val res = ClaimValidation.validate(req)
                errors = res.errors
                if (res.isValid) scope.launch {
                    runCatching { Bff.openClaim(req) }.onSuccess { done = it; errors = emptyMap() }
                }
            }
        }) {
            Div(attrs = { classes(AreaStyles.stack) }) {
                WaInput(label = "Data accadimento", id = "occurredOn", type = "date", hint = errors["occurredOn"]) { occurredOn = it }
                WaTextarea(label = "Descrizione", id = "desc", rows = 4, hint = errors["description"]) { description = it }
                WaButton(type = "submit", variant = "brand", appearance = "accent", pill = true) { Text("Invia sinistro") }
            }
        }
    }
}
