package com.example.insurance.web.pages

import androidx.compose.runtime.*
import com.example.insurance.designsystem.style.LayoutStyles
import com.example.insurance.designsystem.ui.*
import com.example.insurance.designsystem.wa.WaInput
import com.example.insurance.shared.model.Agency
import com.example.insurance.web.Bff
import com.example.insurance.web.ui.PageShell
import com.example.insurance.web.ui.PageStyles
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import org.jetbrains.compose.web.css.Style
import org.jetbrains.compose.web.dom.*

/**
 * Store locator. Dimostra il CARICAMENTO LAZY di una porzione di pagina: guscio + ricerca
 * subito, elenco agenzie da un endpoint lento (~2.5s) con skeleton → contenuto.
 */
@Page("/trova-agenzia")
@Composable
fun TrovaAgenziaPage() {
    val ctx = rememberPageContext()
    val navigate: (String) -> Unit = { ctx.router.navigateTo(it) }
    var state by remember { mutableStateOf<LoadState<List<Agency>>>(LoadState.Loading) }
    var query by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        state = runCatching { Bff.agencies() }.fold(
            onSuccess = { LoadState.Ready(it) },
            onFailure = { LoadState.Error("Impossibile caricare le agenzie. Riprova più tardi.") },
        )
    }

    CompositionLocalProvider(LocalNavigate provides navigate) {
        PageShell(navigate) {
            Style(PageStyles)
            PageSection {
                SectionHead(
                    "Trova agenzia",
                    "La nostra rete sul territorio",
                    "Cerca l'agenzia più vicina. L'elenco arriva da un servizio esterno: " +
                        "il resto della pagina è già pronto mentre la lista si carica.",
                )
                Div(attrs = { classes(PageStyles.locatorSearch); style { property("margin-inline", "auto") } }) {
                    WaInput(label = "Cerca per città o nome", id = "agency-q") { query = it }
                }
                LazyContent(state, skeleton = { SkeletonGrid(count = 6, cols = 3) }) { agencies ->
                    val filtered = agencies.filter {
                        query.isBlank() ||
                            it.city.contains(query, ignoreCase = true) ||
                            it.name.contains(query, ignoreCase = true)
                    }
                    if (filtered.isEmpty()) {
                        P(attrs = { classes("muted") }) { Text("Nessuna agenzia per \"$query\".") }
                    } else {
                        Grid(cols = 3) { filtered.forEach { AgencyCard(it) } }
                    }
                }
            }
        }
    }
}

@Composable
private fun AgencyCard(a: Agency) {
    Article(attrs = { classes(PageStyles.agency) }) {
        P(attrs = { classes(LayoutStyles.eyebrow); style { property("margin", "0 0 4px") } }) { Text(a.city) }
        H3 { Text(a.name) }
        P(attrs = { classes("muted") }) { Text(a.address) }
        A(href = "tel:${a.phone.replace(" ", "")}", attrs = { classes(LayoutStyles.linkArrow) }) { Text(a.phone) }
    }
}
