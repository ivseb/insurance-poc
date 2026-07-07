package com.example.insurance.designsystem.ui

import androidx.compose.runtime.Composable
import com.example.insurance.designsystem.style.AsyncStyles
import com.example.insurance.designsystem.wa.WaCallout
import org.jetbrains.compose.web.css.Style
import org.jetbrains.compose.web.dom.*

/**
 * Stato di un caricamento asincrono di una porzione di pagina.
 * Permette al guscio della pagina di renderizzare SUBITO (parte dello static export),
 * mentre la porzione "lenta" passa da Loading → Ready/Error in modo indipendente.
 */
sealed interface LoadState<out T> {
    data object Loading : LoadState<Nothing>
    data class Error(val message: String) : LoadState<Nothing>
    data class Ready<T>(val value: T) : LoadState<T>
}

/**
 * Renderizza una porzione di pagina in base al suo [LoadState]. Accessibile: aria-busy
 * durante il caricamento, aria-live="polite" per annunciare l'arrivo del contenuto.
 * Monta il proprio chunk di stile (skeleton) → caricato solo dove c'è caricamento lazy.
 */
@Composable
fun <T> LazyContent(
    state: LoadState<T>,
    skeleton: @Composable () -> Unit,
    error: @Composable (String) -> Unit = { DefaultLazyError(it) },
    ready: @Composable (T) -> Unit,
) {
    Style(AsyncStyles)
    Div(attrs = {
        attr("aria-busy", (state is LoadState.Loading).toString())
        attr("aria-live", "polite")
    }) {
        when (state) {
            is LoadState.Loading -> skeleton()
            is LoadState.Error -> error(state.message)
            is LoadState.Ready -> ready(state.value)
        }
    }
}

@Composable
private fun DefaultLazyError(message: String) {
    WaCallout("danger") { Text(message) }
}

/** Placeholder "shimmer" per una card. */
@Composable
fun SkeletonCard() {
    Div(attrs = { classes(AsyncStyles.card) }) {
        Div(attrs = { classes(AsyncStyles.line, AsyncStyles.lineTitle) }) {}
        Div(attrs = { classes(AsyncStyles.line) }) {}
        Div(attrs = { classes(AsyncStyles.line, AsyncStyles.lineShort) }) {}
    }
}

/** Griglia di skeleton: stessa forma della griglia reale, così il layout non "salta". */
@Composable
fun SkeletonGrid(count: Int = 6, cols: Int = 3) {
    Grid(cols = cols) { repeat(count) { SkeletonCard() } }
}
