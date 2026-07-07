package com.example.insurance.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.example.insurance.shared.client.BffClient
import com.example.insurance.shared.model.ClaimRequest
import com.example.insurance.shared.model.PolicyDetail
import com.example.insurance.shared.model.PolicySummary
import com.example.insurance.shared.validation.ClaimValidation
import com.example.insurance.android.ui.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Stessa logica del web: stesso BffClient (:shared), stessi DTO, stessa ClaimValidation.
 * Cambia solo il layer UI (Compose Android). Look allineato al brand ACME / area clienti.
 */
class AppState(private val scope: CoroutineScope) {
    private var token: String? = null
    private val client = BffClient(BuildConfig.BFF_BASE_URL) { token }

    var loggedIn by mutableStateOf(false); private set
    var policies by mutableStateOf<List<PolicySummary>>(emptyList()); private set
    var detail by mutableStateOf<PolicyDetail?>(null); private set
    var error by mutableStateOf<String?>(null); private set

    fun login(user: String, pwd: String) {
        scope.launch {
            runCatching { client.login(user, pwd) }
                .onSuccess { token = it.token; loggedIn = true; policies = client.policies() }
                .onFailure { error = "Accesso non riuscito" }
        }
    }

    fun open(id: String) = scope.launch {
        runCatching { client.policy(id) }.onSuccess { detail = it }
    }

    fun openClaim(req: ClaimRequest, onDone: (String) -> Unit) {
        if (!ClaimValidation.validate(req).isValid) return
        scope.launch { runCatching { client.openClaim(req) }.onSuccess { onDone(it.claimId) } }
    }

    fun back() { detail = null }
    fun logout() { token = null; loggedIn = false; detail = null; policies = emptyList() }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { InsuranceTheme { AppRoot() } }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppRoot() {
    val scope = rememberCoroutineScope()
    val state = remember { AppState(scope) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { BrandLogo() },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White),
                navigationIcon = {
                    if (state.detail != null) TextButton(onClick = state::back) { Text("‹ Indietro") }
                },
                actions = {
                    if (state.loggedIn) TextButton(onClick = state::logout) { Text("Esci") }
                },
            )
        },
    ) { padding ->
        Surface(Modifier.fillMaxSize().padding(padding), color = BrandColors.Bg) {
            when {
                !state.loggedIn -> LoginScreen(state)
                state.detail != null -> PolicyDetailScreen(state.detail!!, state)
                else -> PolicyListScreen(state.policies, onOpen = state::open)
            }
        }
    }
}

@Composable
private fun LoginScreen(state: AppState) {
    Column(Modifier.padding(24.dp)) {
        LoginForm(error = state.error, onLogin = state::login)
    }
}

@Composable
private fun PolicyListScreen(policies: List<PolicySummary>, onOpen: (String) -> Unit) {
    val firstId = policies.firstOrNull()?.id
    Column(
        Modifier.padding(20.dp).verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text("La tua area personale", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.semantics { heading() })

        Row(
            Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            NovitaCard("Convenzioni e vantaggi", "Sconti e servizi dedicati per prenderti cura di te.")
            NovitaCard("Chiedi a un esperto", "Una domanda a un professionista? Ti rispondiamo noi.")
            NovitaCard("Dal nostro blog", "Approfondimenti e guide per scelte consapevoli.")
        }

        Text("Di cosa hai bisogno?", style = MaterialTheme.typography.titleLarge, modifier = Modifier.semantics { heading() })
        Surface(color = BrandColors.GreenTint, shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                ActionButton("📝", "Apri un sinistro", onClick = { firstId?.let(onOpen) }, modifier = Modifier.fillMaxWidth())
                ActionButton("🗂️", "Gestisci i tuoi sinistri", onClick = { firstId?.let(onOpen) }, modifier = Modifier.fillMaxWidth())
            }
        }

        AgencyCard()

        Text("Le mie polizze", style = MaterialTheme.typography.titleLarge, modifier = Modifier.semantics { heading() })
        policies.forEach { p -> PolicyCard(p) { onOpen(p.id) } }
    }
}

@Composable
private fun PolicyDetailScreen(d: PolicyDetail, state: AppState) {
    var claimId by remember { mutableStateOf<String?>(null) }
    Column(
        Modifier.padding(20.dp).verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Surface(color = Color.White, shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) { PolicyDetailView(d) }
        }
        Surface(color = Color.White, shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                ClaimForm(policyId = d.id, claimId = claimId) { req -> state.openClaim(req) { claimId = it } }
            }
        }
    }
}
