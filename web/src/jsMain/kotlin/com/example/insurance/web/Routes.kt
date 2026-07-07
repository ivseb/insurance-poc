package com.example.insurance.web

/** Rotte centralizzate: l'unico punto che conosce i path. */
object Routes {
    const val HOME = "/"
    const val PRODOTTI = "/prodotti"
    const val CHI_SIAMO = "/chi-siamo"
    const val CONTATTI = "/contatti"
    const val TROVA_AGENZIA = "/trova-agenzia"
    const val BLOG = "/blog"
    const val PRIVACY = "/privacy"
    const val COOKIE = "/cookie"
    const val ACCESSIBILITA = "/accessibilita"
    const val DESIGN_SYSTEM = "/design-system"
    const val LOGIN = "/login"
    const val AREA_POLICIES = "/area/policies"
    fun policyDetail(id: String) = "/area/policy?id=$id"
    fun product(key: String) = "/prodotto?key=$key"
}
