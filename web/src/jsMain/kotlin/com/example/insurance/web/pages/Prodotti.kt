package com.example.insurance.web.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.example.insurance.designsystem.ui.*
import com.example.insurance.web.Routes
import com.example.insurance.web.WebDefaults
import com.example.insurance.web.ui.PageShell
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext

private fun audience(key: String): String = when (key) {
    "auto" -> "Per i tuoi viaggi"; "casa" -> "Per la tua casa"; "vita" -> "Per i tuoi cari"; else -> "Per te"
}

@Page("/prodotti")
@Composable
fun ProdottiPage() {
    val ctx = rememberPageContext()
    val navigate: (String) -> Unit = { ctx.router.navigateTo(it) }

    CompositionLocalProvider(LocalNavigate provides navigate) {
        PageShell(navigate) {
            PageSection {
                SectionHead("Prodotti", "Trova la protezione giusta per te", "Tre famiglie di soluzioni, configurabili in base alle tue esigenze.")
                Grid(cols = 3) {
                    WebDefaults.home.products.forEach { p ->
                        ProductCard(p.icon, p.name, p.tagline, DsAction("Scopri e preventiva") { navigate(Routes.product(p.key)) }, badge = audience(p.key))
                    }
                }
            }
        }
    }
}
