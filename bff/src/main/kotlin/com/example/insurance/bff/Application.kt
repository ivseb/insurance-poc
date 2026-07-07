package com.example.insurance.bff

import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import kotlinx.serialization.json.Json

fun main() {
    val port = System.getenv("PORT")?.toIntOrNull() ?: 8080
    embeddedServer(Netty, port = port, host = "0.0.0.0", module = Application::module).start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) { json(Json { ignoreUnknownKeys = true }) }
    install(CallLogging)

    // Security headers di base
    install(io.ktor.server.plugins.defaultheaders.DefaultHeaders) {
        header("X-Content-Type-Options", "nosniff")
        header("X-Frame-Options", "DENY")
        header("Referrer-Policy", "no-referrer")
    }

    // CORS solo per l'origin del web durante lo sviluppo. In prod il web è servito
    // dalla stessa origin (reverse proxy), quindi questo serve solo a `kobweb run`.
    install(CORS) {
        val webOrigin = System.getenv("WEB_ORIGIN") ?: "http://localhost:8081"
        allowHost(webOrigin.removePrefix("http://").removePrefix("https://"))
        allowHeader(io.ktor.http.HttpHeaders.Authorization)
        allowHeader(io.ktor.http.HttpHeaders.ContentType)
        allowMethod(io.ktor.http.HttpMethod.Post)
        allowCredentials = true
    }

    configureSecurity()   // mock JWT
    configureErrors()
    configureRouting()    // tutte le rotte + chiamate s2s in uscita
}
