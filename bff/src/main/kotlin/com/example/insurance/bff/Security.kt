package com.example.insurance.bff

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.HttpHeaders
import io.ktor.http.auth.HttpAuthHeader
import io.ktor.http.auth.parseAuthorizationHeader
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import java.util.Date

const val AUTH_COOKIE = "AUTH"

/**
 * Auth MOCK per la POC: il BFF firma lui un JWT HMAC dopo un login finto.
 * Sostituibile con Keycloak/OIDC senza toccare web/android (cambia solo il BFF).
 */
object MockJwt {
    private val secret = System.getenv("JWT_SECRET") ?: "dev-secret-change-me"
    private const val issuer = "insurance-bff"
    private val algorithm = Algorithm.HMAC256(secret)

    fun issue(username: String): String =
        JWT.create()
            .withIssuer(issuer)
            .withSubject(username)
            .withExpiresAt(Date(System.currentTimeMillis() + 60 * 60 * 1000)) // 1h
            .sign(algorithm)

    fun verifier() = JWT.require(algorithm).withIssuer(issuer).build()
}

fun Application.configureSecurity() {
    install(Authentication) {
        jwt("user") {
            realm = "insurance"
            // Web: JWT in cookie HttpOnly (non leggibile dal JS -> sicuro contro XSS).
            // Android: JWT in header Authorization. Qui accettiamo entrambi.
            authHeader { call ->
                call.request.headers[HttpHeaders.Authorization]?.let {
                    return@authHeader runCatching { parseAuthorizationHeader(it) }.getOrNull()
                }
                call.request.cookies[AUTH_COOKIE]?.let { HttpAuthHeader.Single("Bearer", it) }
            }
            verifier(MockJwt.verifier())
            validate { cred -> cred.payload.subject?.let { JWTPrincipal(cred.payload) } }
        }
    }
}
