package com.example.insurance.web.pages

import androidx.compose.runtime.*
import com.example.insurance.shared.model.CmsHome
import com.example.insurance.web.Bff
import com.example.insurance.web.WebDefaults
import com.example.insurance.web.ui.InstitutionalHome
import com.example.insurance.designsystem.ui.LocalNavigate
import com.example.insurance.web.ui.PageShell
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext

/**
 * Adapter Kobweb (unico file che importa Kobweb per questa pagina).
 * Parte dal contenuto di default (presente nello static export) e lo idrata dal CMS via BFF.
 */
@Page
@Composable
fun HomePage() {
    val ctx = rememberPageContext()
    val navigate: (String) -> Unit = { ctx.router.navigateTo(it) }
    var home by remember { mutableStateOf<CmsHome>(WebDefaults.home) }

    LaunchedEffect(Unit) {
        runCatching { Bff.cmsHome() }.onSuccess { home = it }
    }

    CompositionLocalProvider(LocalNavigate provides navigate) {
        PageShell(navigate) {
            InstitutionalHome(home, navigate)
        }
    }
}
