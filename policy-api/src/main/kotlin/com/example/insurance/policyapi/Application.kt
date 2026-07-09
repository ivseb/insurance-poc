package com.example.insurance.policyapi

import com.example.insurance.shared.model.*
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import io.opentelemetry.instrumentation.ktor.v3_0.KtorServerTelemetry
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json

/**
 * Backend FITTIZIO che simula il sistema assicurativo (polizze/sinistri).
 * È un servizio a sé: il BFF lo raggiunge solo s2s via POLICY_API_URL.
 * In produzione qui ci sarebbe il core assicurativo reale.
 */
fun main() {
    val port = System.getenv("PORT")?.toIntOrNull() ?: 9000
    embeddedServer(Netty, port = port, host = "0.0.0.0") {
        install(KtorServerTelemetry) { setOpenTelemetry(openTelemetry) }
        install(ContentNegotiation) { json(Json { prettyPrint = true }) }
        install(CallLogging)
        routing {
            get("/health") { call.respond(mapOf("status" to "ok")) }
            // SHA del commit di build (env BUILD_SHA) per verificare il drift col codice su main.
            get("/version") { call.respond(mapOf("sha" to (System.getenv("BUILD_SHA") ?: "dev"))) }

            get("/policies") { call.respond(FakeData.summaries) }

            // Endpoint VOLUTAMENTE LENTO (store locator): simula un servizio terzo lento.
            // Il frontend lo carica lazy, mostrando uno skeleton mentre il resto della pagina è già usabile.
            get("/agencies") {
                delay(2500)
                call.respond(FakeData.agencies)
            }

            get("/policies/{id}") {
                val id = call.parameters["id"]
                val detail = FakeData.details[id]
                if (detail == null) call.respond(HttpStatusCode.NotFound, mapOf("error" to "Polizza inesistente"))
                else call.respond(detail)
            }

            post("/claims") {
                val req = call.receive<ClaimRequest>()
                val claimId = "CLM-" + req.policyId.substringAfterLast('-') + "-" +
                    (req.occurredOn.replace("-", "").takeLast(4))
                call.respond(HttpStatusCode.Created, ClaimResponse(claimId = claimId, status = "RECEIVED"))
            }
        }
    }.start(wait = true)
}

/** Dati assicurativi simulati. */
object FakeData {
    val summaries = listOf(
        PolicySummary("pol-001", "Auto", "AUTO-2024-8841", PolicyStatus.ACTIVE),
        PolicySummary("pol-002", "Casa", "CASA-2023-1190", PolicyStatus.EXPIRING),
        PolicySummary("pol-003", "Vita", "VITA-2022-0457", PolicyStatus.ACTIVE),
    )
    val details = mapOf(
        "pol-001" to PolicyDetail(
            "pol-001", "Auto", "AUTO-2024-8841", PolicyStatus.ACTIVE,
            holder = "Mario Rossi", startDate = "2024-03-01", endDate = "2025-03-01", premiumEuro = 612.40,
            coverages = listOf(
                Coverage("RC Auto", 6_000_000.0),
                Coverage("Furto e Incendio", 25_000.0),
                Coverage("Assistenza stradale", 3_000.0),
            ),
        ),
        "pol-002" to PolicyDetail(
            "pol-002", "Casa", "CASA-2023-1190", PolicyStatus.EXPIRING,
            holder = "Mario Rossi", startDate = "2023-09-15", endDate = "2024-09-15", premiumEuro = 289.00,
            coverages = listOf(Coverage("Incendio", 200_000.0), Coverage("Responsabilità civile", 1_000_000.0)),
        ),
        "pol-003" to PolicyDetail(
            "pol-003", "Vita", "VITA-2022-0457", PolicyStatus.ACTIVE,
            holder = "Mario Rossi", startDate = "2022-01-10", endDate = "2042-01-10", premiumEuro = 1_200.00,
            coverages = listOf(Coverage("Capitale caso morte", 150_000.0), Coverage("Invalidità permanente", 100_000.0)),
        ),
    )

    val agencies = listOf(
        Agency("ag-mi", "Milano", "ACME Milano Centro", "Corso Buenos Aires 12, 20124", "+39 02 1234 5678"),
        Agency("ag-to", "Torino", "ACME Torino Crocetta", "Via Sacchi 40, 10128", "+39 011 234 5678"),
        Agency("ag-rm", "Roma", "ACME Roma Eur", "Viale Europa 110, 00144", "+39 06 9876 5432"),
        Agency("ag-na", "Napoli", "ACME Napoli Vomero", "Via Scarlatti 70, 80129", "+39 081 345 6789"),
        Agency("ag-bo", "Bologna", "ACME Bologna Centro", "Via Rizzoli 5, 40125", "+39 051 456 7890"),
        Agency("ag-fi", "Firenze", "ACME Firenze Duomo", "Via dei Calzaiuoli 20, 50122", "+39 055 567 8901"),
    )
}
