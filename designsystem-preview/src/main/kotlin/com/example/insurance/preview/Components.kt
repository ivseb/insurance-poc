package com.example.insurance.preview

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.example.insurance.shared.model.ClaimRequest
import com.example.insurance.shared.model.PolicyDetail
import com.example.insurance.shared.model.PolicySummary
import com.example.insurance.shared.validation.ClaimValidation

/**
 * Componenti Desktop che rispecchiano il design system (gli stessi material3 dell'app Android).
 * Presentazionali: solo dati + callback, così sono "preview-abili".
 */

@Composable
fun PolicyCard(policy: PolicySummary, onClick: () -> Unit) {
    ElevatedCard(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Text("${policy.product} · ${policy.number}", style = MaterialTheme.typography.titleMedium)
            Text("Stato: ${policy.status}")
        }
    }
}

@Composable
fun PolicyDetailView(d: PolicyDetail) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("${d.product} · ${d.number}", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.semantics { heading() })
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
    val errors = remember(occurredOn, description) {
        ClaimValidation.validate(ClaimRequest(policyId, occurredOn, description)).errors
    }
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Apri un sinistro", style = MaterialTheme.typography.titleLarge, modifier = Modifier.semantics { heading() })
        claimId?.let { Text("Sinistro $it ricevuto", color = MaterialTheme.colorScheme.primary) }
        OutlinedTextField(occurredOn, { occurredOn = it }, label = { Text("Data accadimento (AAAA-MM-GG)") },
            isError = errors.containsKey("occurredOn"), modifier = Modifier.fillMaxWidth())
        OutlinedTextField(description, { description = it }, label = { Text("Descrizione") },
            isError = errors.containsKey("description"), minLines = 3, modifier = Modifier.fillMaxWidth())
        Button(
            onClick = { onSubmit(ClaimRequest(policyId, occurredOn, description)) },
            enabled = errors.isEmpty(),
            modifier = Modifier.heightIn(min = 48.dp),
        ) { Text("Invia sinistro") }
    }
}

@Composable
fun LoginForm(error: String?, onLogin: (String, String) -> Unit) {
    var user by remember { mutableStateOf("") }
    var pwd by remember { mutableStateOf("") }
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Accedi", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.semantics { heading() })
        OutlinedTextField(user, { user = it }, label = { Text("Nome utente") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(pwd, { pwd = it }, label = { Text("Password") }, modifier = Modifier.fillMaxWidth())
        error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        Button(onClick = { onLogin(user, pwd) }, modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) {
            Text("Entra")
        }
    }
}
