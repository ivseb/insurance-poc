package com.example.insurance.shared.validation

import com.example.insurance.shared.model.ClaimRequest

/**
 * Regole di validazione condivise: stessa logica su web, Android e (di nuovo) nel BFF.
 * Condividere QUESTO è il vero risparmio di time-to-market: una sola implementazione, zero drift.
 */
object ClaimValidation {

    data class Result(val errors: Map<String, String>) {
        val isValid: Boolean get() = errors.isEmpty()
    }

    private val isoDate = Regex("""\d{4}-\d{2}-\d{2}""")

    fun validate(req: ClaimRequest): Result {
        val errors = buildMap {
            if (req.policyId.isBlank()) put("policyId", "Polizza obbligatoria")
            if (!isoDate.matches(req.occurredOn)) put("occurredOn", "Data nel formato AAAA-MM-GG")
            when {
                req.description.isBlank() -> put("description", "Descrizione obbligatoria")
                req.description.length < 10 -> put("description", "Minimo 10 caratteri")
                req.description.length > 1000 -> put("description", "Massimo 1000 caratteri")
            }
        }
        return Result(errors)
    }
}
