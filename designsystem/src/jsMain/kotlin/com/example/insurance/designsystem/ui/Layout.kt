package com.example.insurance.designsystem.ui

import androidx.compose.runtime.Composable
import com.example.insurance.designsystem.style.LayoutStyles
import org.jetbrains.compose.web.dom.*

/** Larghezza-contenuto condivisa (classe `.container` definita nel layer base). */
@Composable
fun Container(content: @Composable () -> Unit) {
    Div(attrs = { classes("container") }) { content() }
}

@Composable
fun PageSection(alt: Boolean = false, warm: Boolean = false, content: @Composable () -> Unit) {
    val cls = when {
        warm -> LayoutStyles.sectionWarm
        alt -> LayoutStyles.sectionAlt
        else -> LayoutStyles.section
    }
    Section(attrs = { classes(cls) }) { Container { content() } }
}

/** Intestazione sezione: eyebrow rosso + titolo serif. Centrata (un design moderno) o allineata a sinistra. */
@Composable
fun SectionHead(eyebrow: String, title: String, sub: String? = null, centered: Boolean = true) {
    Div(attrs = { classes(if (centered) LayoutStyles.head else LayoutStyles.headLeft) }) {
        P(attrs = { classes(LayoutStyles.eyebrow) }) { Text(eyebrow) }
        H2 { Text(title) }
        sub?.let { P(attrs = { classes("muted") }) { Text(it) } }
    }
}

/** Griglia responsiva (2, 3 o 4 colonne). */
@Composable
fun Grid(cols: Int = 3, content: @Composable () -> Unit) {
    val colCls = when (cols) {
        2 -> LayoutStyles.cols2
        4 -> LayoutStyles.cols4
        else -> LayoutStyles.cols3
    }
    Div(attrs = { classes(LayoutStyles.grid, colCls) }) { content() }
}

/** Card neutra (contenitore). */
@Composable
fun Card(content: @Composable () -> Unit) {
    Div(attrs = { classes(LayoutStyles.card) }) { content() }
}
