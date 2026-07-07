package com.example.insurance.preview

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.insurance.shared.model.*
import androidx.compose.ui.tooling.preview.Preview

/**
 * @Preview di JetBrains (Compose Multiplatform): IntelliJ li renderizza nel pannello
 * "Preview" se hai installato il plugin "Compose Multiplatform IDE Support".
 * Apri questo file e premi il bottone Preview nel gutter / split view.
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

@Preview
@Composable
fun PolicyCardPreview() = PreviewSurface { PolicyCard(sampleSummary) {} }

@Preview
@Composable
fun PolicyDetailPreview() = PreviewSurface { PolicyDetailView(sampleDetail) }

@Preview
@Composable
fun ClaimFormPreview() = PreviewSurface { ClaimForm("pol-001", claimId = null) {} }

@Preview
@Composable
fun ClaimFormDonePreview() = PreviewSurface { ClaimForm("pol-001", claimId = "CLM-001-0601") {} }

@Preview
@Composable
fun LoginFormPreview() = PreviewSurface { LoginForm(error = null) { _, _ -> } }

@Preview
@Composable
fun LoginErrorPreview() = PreviewSurface { LoginForm(error = "Credenziali non valide") { _, _ -> } }
