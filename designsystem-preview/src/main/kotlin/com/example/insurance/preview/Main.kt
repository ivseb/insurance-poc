package com.example.insurance.preview

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.ImageComposeScene
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.example.insurance.shared.model.*
import java.io.File

/**
 * `./gradlew :designsystem-preview:run`  -> apre la gallery in finestra.
 * `RENDER=1 ./gradlew :designsystem-preview:run`  -> renderizza la gallery in PNG (headless).
 */
fun main() {
    if (System.getenv("RENDER") == "1") {
        renderToPng(File("/tmp/ds-preview.png"))
        return
    }
    application {
        Window(onCloseRequest = ::exitApplication, title = "Design System — Anteprima") {
            InsuranceTheme { Surface(Modifier.fillMaxSize()) { Gallery() } }
        }
    }
}

private fun renderToPng(out: File) {
    val scene = ImageComposeScene(width = 960, height = 1500, density = Density(2f)) {
        InsuranceTheme { Surface { Gallery() } }
    }
    val bytes = scene.render().encodeToData()!!.bytes
    out.writeBytes(bytes)
    scene.close()
    println("Render scritto in ${out.absolutePath}")
}

@Composable
private fun Gallery() {
    Column(
        Modifier.padding(24.dp).verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        Text("Design System — Anteprima componenti", style = MaterialTheme.typography.headlineMedium)
        Showcase("Login") { LoginForm(error = null) { _, _ -> } }
        Showcase("Card polizza") { PolicyCard(GallerySamples.summary) {} }
        Showcase("Dettaglio polizza") { PolicyDetailView(GallerySamples.detail) }
        Showcase("Apertura sinistro") { ClaimForm("pol-001", claimId = null) {} }
    }
}

@Composable
private fun Showcase(title: String, content: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(title, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
        ElevatedCard { Box(Modifier.padding(16.dp)) { content() } }
    }
}

object GallerySamples {
    val summary = PolicySummary("pol-001", "Auto", "AUTO-2024-8841", PolicyStatus.ACTIVE)
    val detail = PolicyDetail(
        "pol-001", "Auto", "AUTO-2024-8841", PolicyStatus.ACTIVE,
        holder = "Mario Rossi", startDate = "2024-03-01", endDate = "2025-03-01", premiumEuro = 612.40,
        coverages = listOf(
            Coverage("RC Auto", 6_000_000.0),
            Coverage("Furto e Incendio", 25_000.0),
            Coverage("Assistenza stradale", 3_000.0),
        ),
    )
}
