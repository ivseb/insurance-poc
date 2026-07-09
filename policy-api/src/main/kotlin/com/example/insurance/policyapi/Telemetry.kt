package com.example.insurance.policyapi

import io.opentelemetry.api.OpenTelemetry
import io.opentelemetry.instrumentation.logback.appender.v1_0.OpenTelemetryAppender
import io.opentelemetry.instrumentation.runtimemetrics.java17.RuntimeMetrics
import io.opentelemetry.sdk.autoconfigure.AutoConfiguredOpenTelemetrySdk

/** OpenTelemetry autoconfigurato da env OTEL_*: span server (con estrazione trace context), log e JVM. */
val openTelemetry: OpenTelemetry by lazy {
    val sdk = AutoConfiguredOpenTelemetrySdk.builder().build().openTelemetrySdk
    OpenTelemetryAppender.install(sdk)
    RuntimeMetrics.create(sdk)
    sdk
}
