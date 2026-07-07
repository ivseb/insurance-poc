package com.example.insurance.bff

import com.example.insurance.shared.model.*
import com.example.insurance.shared.validation.ClaimValidation
import io.ktor.http.Cookie
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.*

fun Application.configureRouting() {
    val policyApi = PolicyApi()
    val cms = StrapiCms()

    routing {
        get("/health") { call.respond(mapOf("status" to "ok")) }

        // SHA del commit da cui è stata buildata l'immagine (iniettato in build → env BUILD_SHA).
        // Serve a verificare COSA gira davvero rispetto a main (drift).
        get("/version") { call.respond(mapOf("sha" to (System.getenv("BUILD_SHA") ?: "dev"))) }

        // --- login mock: credenziali non vuote -> JWT firmato dal BFF ---
        // Web: JWT in cookie HttpOnly/SameSite (non leggibile dal JS). Android: usa il token nel body.
        post("/api/auth/login") {
            val req = call.receive<AuthRequest>()
            require(req.username.isNotBlank() && req.password.isNotBlank()) { "Credenziali mancanti" }
            val token = MockJwt.issue(req.username)
            val secure = System.getenv("COOKIE_SECURE")?.toBoolean() ?: false  // true dietro HTTPS in prod
            call.response.cookies.append(
                Cookie(
                    name = AUTH_COOKIE, value = token, httpOnly = true, secure = secure,
                    path = "/", maxAge = 60 * 60, extensions = mapOf("SameSite" to "Lax"),
                )
            )
            call.respond(AuthResponse(token, displayName = req.username))
        }

        post("/api/auth/logout") {
            call.response.cookies.append(Cookie(AUTH_COOKIE, "", path = "/", maxAge = 0))
            call.respond(HttpStatusCode.NoContent)
        }

        // --- contenuti CMS: pubblici, ma SEMPRE mediati dal BFF (s2s verso Strapi) ---
        get("/api/cms/home") {
            call.respond(cms.home())
        }

        // --- store locator: pubblico (no auth), upstream lento → il web lo carica lazy ---
        // no-store: evita che il browser cachi la risposta, così lo skeleton si vede a ogni visita.
        get("/api/agencies") {
            call.response.headers.append(io.ktor.http.HttpHeaders.CacheControl, "no-store")
            call.respond(AgencyListResponse(policyApi.agencies()))
        }

        // --- area personale: protetta da JWT (cookie web o Bearer android) ---
        authenticate("user") {
            get("/api/auth/me") {
                call.respond(mapOf("user" to call.principal<JWTPrincipal>()?.subject))
            }
            get("/api/policies") {
                call.respond(PolicyListResponse(policyApi.list()))
            }
            get("/api/policies/{id}") {
                call.respond(policyApi.detail(call.parameters["id"]!!))
            }
            post("/api/claims") {
                val req = call.receive<ClaimRequest>()
                // stessa validazione del frontend (modulo shared) -> niente drift
                val result = ClaimValidation.validate(req)
                if (!result.isValid) {
                    call.respond(HttpStatusCode.UnprocessableEntity, mapOf("errors" to result.errors))
                    return@post
                }
                call.respond(HttpStatusCode.Created, policyApi.openClaim(req))
            }
        }
    }
}
