package com.example.insurance.android.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.insurance.android.BrandColors
import com.example.insurance.shared.model.ClaimRequest
import com.example.insurance.shared.model.PolicyDetail
import com.example.insurance.shared.model.PolicyStatus
import com.example.insurance.shared.model.PolicySummary
import com.example.insurance.shared.validation.ClaimValidation

/**
 * Componenti UI Android riusabili e presentazionali, allineati al brand ACME
 * (card polizza verdi, badge "copertura attiva", titoli serif). Visibili con @Preview.
 */

fun productIcon(product: String): String = when (product.lowercase()) {
    "auto" -> "🚗"; "casa" -> "🏠"; "vita" -> "💙"; else -> "📄"
}

/** Logo ACME-like: box navy + barra rossa + wordmark. */
@Composable
fun BrandLogo() {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Box(contentAlignment = Alignment.BottomCenter, modifier = Modifier.size(40.dp, 34.dp).clip(RoundedCornerShape(3.dp)).background(BrandColors.Navy)) {
            Text("ACME", color = androidx.compose.ui.graphics.Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 11.sp)
            Box(Modifier.fillMaxWidth().height(5.dp).background(BrandColors.Red))
        }
        Text("Assicurazioni", fontWeight = FontWeight.Bold, color = BrandColors.Ink)
    }
}

@Composable
fun StatusBadge(status: PolicyStatus) {
    val (color, label) = when (status) {
        PolicyStatus.ACTIVE -> BrandColors.Green to "✓ Copertura attiva"
        PolicyStatus.EXPIRING -> BrandColors.Amber to "In scadenza"
        PolicyStatus.EXPIRED -> BrandColors.Red to "Scaduta"
    }
    Text(label, color = color, fontWeight = FontWeight.Bold, fontSize = 13.sp)
}

/** Card polizza verde tenue (stile areaclienti): header + nome serif sottolineato. */
@Composable
fun PolicyCard(policy: PolicySummary, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = BrandColors.GreenTint),
        border = BorderStroke(1.dp, BrandColors.GreenLine),
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(Modifier.size(44.dp).clip(CircleShape).background(androidx.compose.ui.graphics.Color.White), contentAlignment = Alignment.Center) {
                Text(productIcon(policy.product), fontSize = 20.sp)
            }
            Column(Modifier.weight(1f)) {
                Text("ACME Assicurazioni S.p.A.", color = BrandColors.Slate, fontSize = 12.sp)
                Text("Protezione ${policy.product}", fontWeight = FontWeight.Bold)
            }
            StatusBadge(policy.status)
        }
        Surface(color = androidx.compose.ui.graphics.Color.White, modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text(
                    "${policy.product} · ${policy.number}",
                    style = MaterialTheme.typography.titleLarge,
                    color = BrandColors.Navy,
                    textDecoration = TextDecoration.Underline,
                )
                Spacer(Modifier.height(6.dp))
                Text("Vedi dettaglio →", color = BrandColors.Navy, fontWeight = FontWeight.Bold)
            }
        }
    }
}

/** CTA verde a pillola (Apri/Gestisci sinistro). */
@Composable
fun ActionButton(icon: String, label: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Surface(onClick = onClick, shape = RoundedCornerShape(50), color = BrandColors.GreenBtn, modifier = modifier) {
        Row(Modifier.padding(horizontal = 16.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(Modifier.size(40.dp).clip(CircleShape).background(androidx.compose.ui.graphics.Color.White), contentAlignment = Alignment.Center) { Text(icon, fontSize = 18.sp) }
            Text(label, color = BrandColors.Navy, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            Text("›", color = BrandColors.Navy, fontSize = 20.sp)
        }
    }
}

/** Card NOVITÀ (informativa): l'app non ha le pagine editoriali, quindi niente CTA morta. */
@Composable
fun NovitaCard(title: String, text: String, modifier: Modifier = Modifier) {
    Card(
        modifier.width(260.dp), shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = androidx.compose.ui.graphics.Color.White),
        border = BorderStroke(1.dp, BrandColors.Line),
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text("NOVITÀ", color = BrandColors.Red, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Text(title, fontWeight = FontWeight.Bold)
            Text(text, color = BrandColors.Slate, fontSize = 14.sp)
        }
    }
}

/** Card "La mia Agenzia". */
@Composable
fun AgencyCard() {
    Card(
        Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = androidx.compose.ui.graphics.Color.White),
        border = BorderStroke(1.dp, BrandColors.Line),
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("LA MIA AGENZIA", color = BrandColors.Slate, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Text("ACME Milano Centro", fontWeight = FontWeight.Bold)
            Text("Corso Buenos Aires 12, 20124 Milano", color = BrandColors.Slate)
            Text("Tel: +39 02 1234 5678")
        }
    }
}

@Composable
fun PolicyDetailView(d: PolicyDetail) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(Modifier.size(48.dp).clip(CircleShape).background(BrandColors.NavyTint), contentAlignment = Alignment.Center) { Text(productIcon(d.product), fontSize = 22.sp) }
            Column { Text("Polizza n° ${d.number}", color = BrandColors.Slate, fontSize = 13.sp); Text("Protezione ${d.product}", fontWeight = FontWeight.Bold) }
            Spacer(Modifier.weight(1f))
            StatusBadge(d.status)
        }
        Text("${d.product} · ${d.number}", style = MaterialTheme.typography.headlineMedium, color = BrandColors.Navy, modifier = Modifier.semantics { heading() })
        Text("Contraente: ${d.holder}")
        Text("Validità: ${d.startDate} → ${d.endDate}")
        Text("Premio annuo: € ${d.premiumEuro}")
        Text("Garanzie", style = MaterialTheme.typography.titleLarge, modifier = Modifier.semantics { heading() })
        d.coverages.forEach { c -> Text("• ${c.name} — massimale € ${c.ceilingEuro}") }
    }
}

@Composable
fun ClaimForm(policyId: String, claimId: String?, onSubmit: (ClaimRequest) -> Unit) {
    var occurredOn by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    // Gli errori si mostrano solo DOPO un tentativo di invio (come il web): niente campi rossi
    // appena si apre il dettaglio.
    var submitted by remember { mutableStateOf(false) }
    val errors = ClaimValidation.validate(ClaimRequest(policyId, occurredOn, description)).errors
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Apri un sinistro", style = MaterialTheme.typography.titleLarge, modifier = Modifier.semantics { heading() })
        claimId?.let { Text("Sinistro $it ricevuto", color = BrandColors.Green, fontWeight = FontWeight.Bold) }
        OutlinedTextField(occurredOn, { occurredOn = it }, label = { Text("Data accadimento (AAAA-MM-GG)") },
            isError = submitted && errors.containsKey("occurredOn"),
            supportingText = { if (submitted) errors["occurredOn"]?.let { Text(it) } },
            modifier = Modifier.fillMaxWidth())
        OutlinedTextField(description, { description = it }, label = { Text("Descrizione") },
            isError = submitted && errors.containsKey("description"),
            supportingText = { if (submitted) errors["description"]?.let { Text(it) } },
            minLines = 3, modifier = Modifier.fillMaxWidth())
        Button(
            onClick = { submitted = true; if (errors.isEmpty()) onSubmit(ClaimRequest(policyId, occurredOn, description)) },
            shape = RoundedCornerShape(50),
            modifier = Modifier.heightIn(min = 48.dp),
        ) { Text("Invia sinistro") }
    }
}

@Composable
fun LoginForm(error: String?, onLogin: (String, String) -> Unit) {
    var user by remember { mutableStateOf("") }
    var pwd by remember { mutableStateOf("") }
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Accedi all'area personale", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.semantics { heading() })
        OutlinedTextField(user, { user = it }, label = { Text("Nome utente") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(pwd, { pwd = it }, label = { Text("Password") }, modifier = Modifier.fillMaxWidth())
        error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        Button(onClick = { onLogin(user, pwd) }, shape = RoundedCornerShape(50), modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) {
            Text("Entra")
        }
    }
}
