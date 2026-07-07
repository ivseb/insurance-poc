package com.example.insurance.shared.client

import com.example.insurance.shared.model.*
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * Unico client usato da web e Android: parla SOLO col BFF.
 * Il browser/app non conoscono né Strapi né l'API polizze: quelle chiamate sono s2s nel BFF.
 */
class BffClient(
    private val baseUrl: String,
    private val tokenProvider: () -> String? = { null },
) {
    private val http: HttpClient = createHttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    private fun HttpRequestBuilder.auth() {
        tokenProvider()?.let { header("Authorization", "Bearer $it") }
    }

    suspend fun login(username: String, password: String): AuthResponse =
        http.post("$baseUrl/api/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(AuthRequest(username, password))
        }.body()

    suspend fun policies(): List<PolicySummary> =
        http.get("$baseUrl/api/policies") { auth() }.body<PolicyListResponse>().policies

    suspend fun policy(id: String): PolicyDetail =
        http.get("$baseUrl/api/policies/$id") { auth() }.body()

    suspend fun openClaim(req: ClaimRequest): ClaimResponse =
        http.post("$baseUrl/api/claims") {
            auth(); contentType(ContentType.Application.Json); setBody(req)
        }.body()

    suspend fun cmsHome(): CmsHome =
        http.get("$baseUrl/api/cms/home").body()
}

/** Engine Ktor specifico per piattaforma (js / okhttp). */
expect fun createHttpClient(config: HttpClientConfig<*>.() -> Unit = {}): HttpClient
