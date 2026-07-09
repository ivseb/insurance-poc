package com.example.insurance.bff

import com.example.insurance.shared.model.*
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * Chiamate USCENTI server-to-server. I token vivono SOLO qui (variabili d'ambiente del BFF),
 * non raggiungono mai browser/app. È questo che soddisfa il requisito "mai dal frontend".
 */
private val outbound = HttpClient(OkHttp) {
    // Propaga il trace context alle chiamate s2s (bff→policy-api) → trace distribuita correlata.
    install(io.opentelemetry.instrumentation.ktor.v3_0.KtorClientTelemetry) { setOpenTelemetry(openTelemetry) }
    install(ContentNegotiation) { json(Json { ignoreUnknownKeys = true }) }
}

/** API polizze/sinistri. Per la POC, se POLICY_API_URL non è settata usa dati mock embedded. */
class PolicyApi {
    private val baseUrl = System.getenv("POLICY_API_URL") ?: "http://localhost:9000"
    private val token = System.getenv("POLICY_API_TOKEN") ?: ""

    private fun HttpRequestBuilder.auth() {
        if (token.isNotBlank()) header("Authorization", "Bearer $token")
    }

    /** git-SHA di build di policy-api (endpoint /version), per il check "cosa c'è su". */
    suspend fun version(): String =
        outbound.get("$baseUrl/version") { auth() }.body<Map<String, String>>()["sha"] ?: "unknown"

    suspend fun list(): List<PolicySummary> =
        outbound.get("$baseUrl/policies") { auth() }.body()

    /** Store locator (pubblico): l'upstream è lento di proposito → il web lo carica lazy. */
    suspend fun agencies(): List<Agency> =
        outbound.get("$baseUrl/agencies") { auth() }.body()

    suspend fun detail(id: String): PolicyDetail =
        outbound.get("$baseUrl/policies/$id") { auth() }.body()

    suspend fun openClaim(req: ClaimRequest): ClaimResponse =
        outbound.post("$baseUrl/claims") {
            auth(); contentType(ContentType.Application.Json); setBody(req)
        }.body()
}

/** Strapi (CMS): contenuti istituzionali. Token API solo server-side (mai al frontend). */
class StrapiCms {
    private val baseUrl = System.getenv("STRAPI_URL")              // es. http://localhost:1337
    private val token = System.getenv("STRAPI_TOKEN") ?: ""

    suspend fun home(): CmsHome {
        if (baseUrl == null) return Mock.home   // fallback per girare senza Strapi
        // Strapi v5 REST, single type "homepage" con componenti products/features popolati.
        val resp = outbound.get("$baseUrl/api/homepage?populate=*") {
            if (token.isNotBlank()) header("Authorization", "Bearer $token")
        }.body<StrapiHomeResp>()
        val d = resp.data ?: throw IllegalArgumentException("Homepage non trovata su Strapi")
        return CmsHome(
            heroEyebrow = d.heroEyebrow, heroTitle = d.heroTitle,
            heroSubtitle = d.heroSubtitle, heroCtaLabel = d.heroCtaLabel,
            products = d.products.map { CmsProduct(it.key, it.name, it.tagline, it.icon) },
            features = d.features.map { CmsFeature(it.title, it.text) },
        )
    }
}

// ---- forma reale della risposta Strapi v5 -> DTO condiviso (mapping nel BFF) ----
@kotlinx.serialization.Serializable
private data class StrapiHomeResp(val data: StrapiHomeData? = null)
@kotlinx.serialization.Serializable
private data class StrapiHomeData(
    val heroEyebrow: String = "", val heroTitle: String = "", val heroSubtitle: String = "", val heroCtaLabel: String = "",
    val products: List<StrapiProduct> = emptyList(), val features: List<StrapiFeature> = emptyList(),
)
@kotlinx.serialization.Serializable
private data class StrapiProduct(val key: String = "", val name: String = "", val tagline: String = "", val icon: String = "")
@kotlinx.serialization.Serializable
private data class StrapiFeature(val title: String = "", val text: String = "")

/** Fallback CMS per girare senza Strapi (i dati polizza vivono nel servizio :policy-api). */
private object Mock {
    // Titolo volutamente diverso dal default web: così si "vede" l'idratazione dal CMS via BFF.
    val home = CmsHome(
        heroEyebrow = "ACME · contenuto dal BFF",
        heroTitle = "Il futuro inizia dai nostri sogni",
        heroSubtitle = "Polizze auto, casa e vita pensate per la vita reale. Gestisci tutto dall'area personale.",
        heroCtaLabel = "Fai un preventivo",
        products = listOf(
            CmsProduct("auto", "Auto", "RC, furto e assistenza stradale per viaggiare sereno.", "🚗"),
            CmsProduct("casa", "Casa", "Proteggi la tua abitazione da incendio, furto e danni.", "🏠"),
            CmsProduct("vita", "Vita", "Tutela per chi ami e soluzioni di risparmio.", "💙"),
        ),
        features = listOf(
            CmsFeature("Tutto online", "Polizze, documenti e sinistri in pochi tocchi, 24/7."),
            CmsFeature("Assistenza vera", "Una rete di agenti sul territorio quando serve davvero."),
            CmsFeature("Trasparenza", "Coperture chiare, nessuna sorpresa."),
        ),
    )
}
