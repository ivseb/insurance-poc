package com.example.insurance.web.pages

import androidx.compose.runtime.*
import com.example.insurance.designsystem.style.AreaStyles
import com.example.insurance.web.Bff
import com.example.insurance.web.Routes
import com.example.insurance.web.ui.AppShell
import com.example.insurance.designsystem.ui.LocalNavigate
import com.example.insurance.designsystem.wa.WaButton
import com.example.insurance.designsystem.wa.WaCallout
import com.example.insurance.designsystem.wa.WaInput
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.attributes.onSubmit
import org.jetbrains.compose.web.dom.*

@Page
@Composable
fun LoginPage() {
    val ctx = rememberPageContext()
    val navigate: (String) -> Unit = { ctx.router.navigateTo(it) }
    val scope = rememberCoroutineScope()
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    CompositionLocalProvider(LocalNavigate provides navigate) {
        AppShell(navigate) {
            Div(attrs = { style { property("max-width", "440px"); property("margin-inline", "auto") } }) {
                H1 { Text("Accedi all'area personale") }
                Div(attrs = { classes(AreaStyles.authCard) }) {
                    Form(attrs = {
                        onSubmit { ev ->
                            ev.preventDefault()
                            scope.launch {
                                runCatching { Bff.login(username, password) }
                                    .onSuccess { navigate(Routes.AREA_POLICIES) }
                                    .onFailure { error = "Credenziali non valide" }
                            }
                        }
                    }) {
                        Div(attrs = { classes(AreaStyles.stack) }) {
                            WaInput(label = "Nome utente", id = "user", autocomplete = "username", required = true) { username = it }
                            WaInput(label = "Password", id = "pwd", type = "password", autocomplete = "current-password", required = true) { password = it }
                            error?.let { WaCallout("danger") { Text(it) } }
                            WaButton(type = "submit", variant = "brand", appearance = "accent", pill = true) { Text("Entra") }
                        }
                    }
                }
            }
        }
    }
}
