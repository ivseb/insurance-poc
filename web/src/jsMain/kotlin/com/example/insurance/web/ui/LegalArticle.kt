package com.example.insurance.web.ui

import androidx.compose.runtime.Composable
import com.example.insurance.designsystem.style.LayoutStyles
import com.example.insurance.designsystem.ui.PageSection
import com.example.insurance.web.content.LegalDoc
import org.jetbrains.compose.web.css.Style
import org.jetbrains.compose.web.dom.*

/** Resa comune delle pagine legali (privacy, cookie, accessibilità) da un [LegalDoc]. */
@Composable
fun LegalArticle(doc: LegalDoc) {
    Style(PageStyles)
    PageSection {
        Div(attrs = { classes(PageStyles.legal); style { property("margin", "0 auto") } }) {
            P(attrs = { classes(LayoutStyles.eyebrow) }) { Text(doc.eyebrow) }
            H1 { Text(doc.title) }
            P(attrs = { classes("muted") }) { Text(doc.updated) }
            doc.sections.forEach { (heading, body) ->
                H2 { Text(heading) }
                P { Text(body) }
            }
        }
    }
}
