    package com.example.insurance.android.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.insurance.android.InsuranceTheme
import com.example.insurance.shared.model.*

/**
 * Annotatore @Preview: apri questi file in Android Studio e usa il pannello "Split/Design"
 * per vedere ogni componente in anteprima, senza avviare l'app.
 *
 * NB: @Preview è disponibile SOLO per Compose Android/Desktop. I componenti web (Compose HTML)
 * non hanno un equivalente IDE: per quelli c'è la gallery live su /design-system.
 */

private val sampleSummary = PolicySummary("pol-001", "Auto", "AUTO-2024-8841", PolicyStatus.ACTIVE)
private val sampleDetail = PolicyDetail(
    "pol-001", "Auto", "AUTO-2024-8841", PolicyStatus.ACTIVE,
    holder = "Mario Rossi", startDate = "2024-03-01", endDate = "2025-03-01", premiumEuro = 612.40,
    coverages = listOf(
        Coverage("RC Auto", 6_000_000.0),
        Coverage("Furto e Incendio", 25_000.0),
        Coverage("Assistenza stradale", 3_000.0),
    ),
)

@Composable
private fun PreviewSurface(content: @Composable () -> Unit) {
    InsuranceTheme { Surface { Box(Modifier.padding(16.dp)) { content() } } }
}

@Preview(name = "Polizza · card", showBackground = true)
@Composable
fun PolicyCardPreview() = PreviewSurface { PolicyCard(sampleSummary) {} }

@Preview(name = "Dettaglio polizza", showBackground = true, heightDp = 420)
@Composable
fun PolicyDetailPreview() = PreviewSurface { PolicyDetailView(sampleDetail) }

@Preview(name = "Apertura sinistro", showBackground = true, heightDp = 420)
@Composable
fun ClaimFormPreview() = PreviewSurface { ClaimForm("pol-001", claimId = null) {} }

@Preview(name = "Sinistro · inviato", showBackground = true, heightDp = 420)
@Composable
fun ClaimFormDonePreview() = PreviewSurface { ClaimForm("pol-001", claimId = "CLM-001-0601") {} }

@Preview(name = "Login", showBackground = true)
@Composable
fun LoginFormPreview() = PreviewSurface { LoginForm(error = null) { _, _ -> } }

@Preview(name = "Login · errore", showBackground = true)
@Composable
fun LoginErrorPreview() = PreviewSurface { LoginForm(error = "Credenziali non valide") { _, _ -> } }
