package com.example.insurance.bff

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.respond
import kotlinx.serialization.Serializable

@Serializable
data class ApiError(val message: String)

fun Application.configureErrors() {
    install(StatusPages) {
        exception<IllegalArgumentException> { call, cause ->
            call.respond(HttpStatusCode.BadRequest, ApiError(cause.message ?: "Richiesta non valida"))
        }
        exception<Throwable> { call, cause ->
            call.application.log.error("Errore non gestito", cause)
            call.respond(HttpStatusCode.InternalServerError, ApiError("Errore interno"))
        }
    }
}
