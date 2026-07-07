package com.example.insurance.web.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.example.insurance.designsystem.style.LayoutStyles
import com.example.insurance.designsystem.ui.*
import com.example.insurance.web.content.SiteContent
import com.example.insurance.web.ui.PageShell
import com.example.insurance.web.ui.PageStyles
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import org.jetbrains.compose.web.css.Style
import org.jetbrains.compose.web.dom.*

@Page("/blog")
@Composable
fun BlogPage() {
    val ctx = rememberPageContext()
    val navigate: (String) -> Unit = { ctx.router.navigateTo(it) }

    CompositionLocalProvider(LocalNavigate provides navigate) {
        PageShell(navigate) {
            Style(PageStyles)
            PageSection {
                SectionHead("Blog", "Consigli e novità", "Brevi guide su auto, casa e vita per scelte più consapevoli.")
                Div(attrs = { classes(PageStyles.articles); style { property("margin", "0 auto") } }) {
                    SiteContent.blog.forEach { post ->
                        Article(attrs = { classes(LayoutStyles.card, PageStyles.post) }) {
                            Div(attrs = { classes(PageStyles.postMeta) }) {
                                Span(attrs = { classes(LayoutStyles.eyebrow); style { property("margin", "0") } }) { Text(post.tag) }
                                Span(attrs = { classes("muted") }) { Text(post.date) }
                            }
                            H2 { Text(post.title) }
                            P(attrs = { classes("muted") }) { Text(post.excerpt) }
                            post.body.forEach { para -> P { Text(para) } }
                        }
                    }
                }
            }
        }
    }
}
