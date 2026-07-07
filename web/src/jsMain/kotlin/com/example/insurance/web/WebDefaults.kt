package com.example.insurance.web

import com.example.insurance.shared.model.CmsFeature
import com.example.insurance.shared.model.CmsHome
import com.example.insurance.shared.model.CmsProduct

/**
 * Contenuto istituzionale di base (renderizzato nello static export, quindi nell'HTML
 * pre-renderizzato → SEO/perf). Il CMS (Strapi via BFF) può sovrascriverlo a runtime.
 */
object WebDefaults {
    val home = CmsHome(
        heroEyebrow = "ACME Assicurazioni",
        heroTitle = "Il futuro inizia da chi proteggi",
        heroSubtitle = "Polizze auto, casa e vita pensate per la vita reale. Gestisci tutto dalla tua area personale.",
        heroCtaLabel = "Fai un preventivo",
        products = listOf(
            CmsProduct("auto", "Auto", "RC, furto e assistenza stradale per viaggiare sereno.", "🚗"),
            CmsProduct("casa", "Casa", "Proteggi la tua abitazione da incendio, furto e danni.", "🏠"),
            CmsProduct("vita", "Vita", "Tutela per chi ami e soluzioni di risparmio.", "💙"),
        ),
        features = listOf(
            CmsFeature("Tutto online", "Polizze, documenti e sinistri gestibili in pochi tocchi, 24/7."),
            CmsFeature("Assistenza vera", "Una rete di agenti sul territorio quando serve davvero."),
            CmsFeature("Trasparenza", "Coperture chiare, nessuna sorpresa: sai sempre cosa hai."),
        ),
    )
}
