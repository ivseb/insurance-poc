package com.example.insurance.bff

import io.opentelemetry.api.OpenTelemetry
import io.opentelemetry.instrumentation.logback.appender.v1_0.OpenTelemetryAppender
import io.opentelemetry.instrumentation.runtimemetrics.java17.RuntimeMetrics
import io.opentelemetry.sdk.autoconfigure.AutoConfiguredOpenTelemetrySdk

/**
 * Un'unica istanza OpenTelemetry, autoconfigurata dalle env `OTEL_*` (endpoint/servizio/protocollo).
 * Condivisa da KtorServerTelemetry (span in ingresso) e KtorClientTelemetry (propagazione s2s):
 * è questo che rende la trace distribuita CORRELATA (edge → bff → policy-api), a differenza dell'agent.
 * Registra anche l'appender OTLP per i log e le metriche JVM.
 */
val openTelemetry: OpenTelemetry by lazy {
    val sdk = AutoConfiguredOpenTelemetrySdk.builder().build().openTelemetrySdk
    OpenTelemetryAppender.install(sdk)   // log via OTLP, correlati col trace_id
    RuntimeMetrics.create(sdk)           // metriche JVM
    sdk
}
