package com.example.insurance.web.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.example.insurance.designsystem.ui.*
import com.example.insurance.web.ui.PageShell
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import org.jetbrains.compose.web.dom.*

@Page("/chi-siamo")
@Composable
fun ChiSiamoPage() {
    val ctx = rememberPageContext()
    val navigate: (String) -> Unit = { ctx.router.navigateTo(it) }

    CompositionLocalProvider(LocalNavigate provides navigate) {
        PageShell(navigate) {
            PageSection {
                SectionHead("Chi siamo?", "Una compagnia vicina alle persone")
                P { Text("ACME Assicurazioni è una POC dimostrativa: un'assicurazione moderna che mette al centro la semplicità, la trasparenza e l'assistenza reale.") }
                P { Text("Operiamo su tre linee — Auto, Casa e Vita — con un'area personale digitale per gestire polizze e sinistri in autonomia.") }
            }
            PageSection(alt = true) {
                Grid(cols = 3) {
                    FeatureItem(1, "Persone prima di tutto", "Prodotti chiari e assistenza umana quando serve.")
                    FeatureItem(2, "Tecnologia al servizio", "Tutto online, accessibile e veloce.")
                    FeatureItem(3, "Trasparenza", "Coperture esplicite, nessuna sorpresa.")
                }
            }
        }
    }
}

