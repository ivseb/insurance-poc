package com.example.insurance.web

import com.example.insurance.shared.model.*
import kotlinx.browser.window
import kotlinx.coroutines.await
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.w3c.fetch.RequestInit

/**
 * Accesso al BFF dal web tramite fetch nativo con `credentials: include`.
 * L'auth viaggia su un cookie HttpOnly settato dal BFF: il JS NON vede mai il token
 * (niente localStorage) -> niente furto del token via XSS.
 */
object Bff {
    class Unauthorized : RuntimeException("Non autenticato")

    // In dev il BFF è su :8080; in prod stessa origin dietro reverse proxy.
    private val base = if (window.location.hostname == "localhost") "http://localhost:8080" else ""
    private val codec = Json { ignoreUnknownKeys = true }

    private suspend fun call(path: String, method: String, body: String? = null): String {
        val opts = js("({})")
        opts.method = method
        opts.credentials = "include"                                   // invia/riceve il cookie
        opts.headers = kotlin.js.json("Content-Type" to "application/json")
        if (body != null) opts.body = body
        val res = window.fetch(base + path, opts.unsafeCast<RequestInit>()).await()
        if (res.status.toInt() == 401) throw Unauthorized()
        if (!res.ok) throw RuntimeException("HTTP ${res.status}")
        return res.text().await()
    }

    suspend fun login(username: String, password: String) {
        call("/api/auth/login", "POST", codec.encodeToString(AuthRequest(username, password)))
    }

    suspend fun logout() = run { call("/api/auth/logout", "POST"); Unit }

    suspend fun cmsHome(): CmsHome = codec.decodeFromString(call("/api/cms/home", "GET"))

    /** Store locator: l'upstream è lento di proposito → la pagina lo carica lazy con skeleton. */
    suspend fun agencies(): List<Agency> =
        codec.decodeFromString<AgencyListResponse>(call("/api/agencies", "GET")).agencies

    suspend fun policies(): List<PolicySummary> =
        codec.decodeFromString<PolicyListResponse>(call("/api/policies", "GET")).policies

    suspend fun policy(id: String): PolicyDetail =
        codec.decodeFromString(call("/api/policies/$id", "GET"))

    suspend fun openClaim(req: ClaimRequest): ClaimResponse =
        codec.decodeFromString(call("/api/claims", "POST", codec.encodeToString(req)))
}
