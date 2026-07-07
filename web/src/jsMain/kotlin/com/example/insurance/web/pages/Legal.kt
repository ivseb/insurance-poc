package com.example.insurance.web.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.example.insurance.designsystem.ui.LocalNavigate
import com.example.insurance.web.content.SiteContent
import com.example.insurance.web.ui.LegalArticle
import com.example.insurance.web.ui.PageShell
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext

/** Pagine legali: stessa resa, contenuto diverso (SiteContent.legal). */
@Composable
private fun LegalPage(docKey: String) {
    val ctx = rememberPageContext()
    val navigate: (String) -> Unit = { ctx.router.navigateTo(it) }
    val doc = SiteContent.legal.getValue(docKey)
    CompositionLocalProvider(LocalNavigate provides navigate) {
        PageShell(navigate) { LegalArticle(doc) }
    }
}

@Page("/privacy")
@Composable
fun PrivacyPage() = LegalPage("privacy")

@Page("/cookie")
@Composable
fun CookiePage() = LegalPage("cookie")

@Page("/accessibilita")
@Composable
fun AccessibilitaPage() = LegalPage("accessibilita")
