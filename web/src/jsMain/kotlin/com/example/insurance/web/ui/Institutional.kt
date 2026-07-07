package com.example.insurance.web.ui

import androidx.compose.runtime.Composable
import com.example.insurance.designsystem.style.LayoutStyles
import com.example.insurance.designsystem.ui.*
import com.example.insurance.designsystem.wa.WaButton
import com.example.insurance.shared.model.CmsHome
import com.example.insurance.web.Routes
import org.jetbrains.compose.web.dom.*

/** Badge "audience" stile moderno per le card prodotto. */
private fun audience(key: String): String = when (key) {
    "auto" -> "Per i tuoi viaggi"
    "casa" -> "Per la tua casa"
    "vita" -> "Per i tuoi cari"
    else -> "Per te"
}

/** Pagina istituzionale (home): assembla i componenti del design system con i dati CMS. */
@Composable
fun InstitutionalHome(home: CmsHome, navigate: (String) -> Unit) {
    Hero(
        eyebrow = home.heroEyebrow,
        title = home.heroTitle,
        subtitle = home.heroSubtitle,
        primary = DsAction(home.heroCtaLabel) { navigate(Routes.PRODOTTI) },
        secondary = DsAction("Area Clienti") { navigate(Routes.LOGIN) },
    )

    PageSection {
        SectionHead("I nostri prodotti", "Soluzioni pensate per la vita reale")
        Grid(cols = 3) {
            home.products.forEach { p ->
                ProductCard(p.icon, p.name, p.tagline, DsAction("Scopri") { navigate(Routes.product(p.key)) }, badge = audience(p.key))
            }
        }
    }

    PageSection(alt = true) {
        SectionHead("Perché ACME", "Vicini quando conta")
        Grid(cols = 3) {
            home.features.forEachIndexed { i, f -> FeatureItem(i + 1, f.title, f.text) }
        }
    }

    Div(attrs = { classes(LayoutStyles.ctaBand) }) {
        Div(attrs = { classes(LayoutStyles.ctaInner) }) {
            H2(attrs = { style { property("color", "#fff"); property("margin", "0") } }) { Text("Hai già una polizza con noi?") }
            WaButton(variant = "brand", appearance = "accent", pill = true, size = "large", onClick = { navigate(Routes.LOGIN) }) {
                Text("Entra nell'area personale")
            }
        }
    }
}
