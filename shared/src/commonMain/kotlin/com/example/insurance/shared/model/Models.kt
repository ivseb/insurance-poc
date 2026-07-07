package com.example.insurance.shared.model

import kotlinx.serialization.Serializable

/**
 * DTO condivisi tra BFF (jvm), web (js) e Android.
 * Sono l'unica "fonte di verità" del contratto: il BFF li serializza, i client li deserializzano.
 */

@Serializable
data class AuthRequest(val username: String, val password: String)

@Serializable
data class AuthResponse(val token: String, val displayName: String)

/** Wrapper: evita la deserializzazione di un List<> top-level (problematica con ktor-client-js). */
@Serializable
data class PolicyListResponse(val policies: List<PolicySummary>)

@Serializable
data class PolicySummary(
    val id: String,
    val product: String,        // es. "Auto", "Casa", "Vita"
    val number: String,         // numero polizza
    val status: PolicyStatus,
)

@Serializable
enum class PolicyStatus { ACTIVE, EXPIRING, EXPIRED }

@Serializable
data class PolicyDetail(
    val id: String,
    val product: String,
    val number: String,
    val status: PolicyStatus,
    val holder: String,
    val startDate: String,      // ISO yyyy-MM-dd (string per semplicità POC, niente dipendenze date KMP)
    val endDate: String,
    val premiumEuro: Double,
    val coverages: List<Coverage>,
)

@Serializable
data class Coverage(val name: String, val ceilingEuro: Double)

/** Apertura sinistro (input dal frontend, validato sia client-side sia nel BFF). */
@Serializable
data class ClaimRequest(
    val policyId: String,
    val occurredOn: String,     // ISO yyyy-MM-dd
    val description: String,
)

@Serializable
data class ClaimResponse(val claimId: String, val status: String)

/** Contenuti istituzionali dal CMS (Strapi), passati sempre via BFF (mai dal frontend). */
@Serializable
data class CmsHome(
    val heroEyebrow: String,
    val heroTitle: String,
    val heroSubtitle: String,
    val heroCtaLabel: String,
    val products: List<CmsProduct>,
    val features: List<CmsFeature>,
)

@Serializable
data class CmsProduct(val key: String, val name: String, val tagline: String, val icon: String)

@Serializable
data class CmsFeature(val title: String, val text: String)

/** Agenzia sul territorio (store locator). Arriva dal backend via un endpoint volutamente LENTO,
 *  per dimostrare il caricamento lazy di una porzione di pagina. */
@Serializable
data class Agency(
    val id: String,
    val city: String,
    val name: String,
    val address: String,
    val phone: String,
)

/** Wrapper: evita la deserializzazione di un List<> top-level (problematica con ktor-client-js). */
@Serializable
data class AgencyListResponse(val agencies: List<Agency>)
