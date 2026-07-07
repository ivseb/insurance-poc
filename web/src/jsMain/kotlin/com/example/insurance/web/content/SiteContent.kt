package com.example.insurance.web.content

/**
 * Contenuti istituzionali statici della POC (prodotti, pagine legali, blog).
 * In produzione arriverebbero dal CMS (Strapi) via BFF; qui sono inline per restare
 * nello static export (HTML pre-renderizzato → SEO/perf), senza chiamate a runtime.
 */

data class ProductPage(
    val key: String,
    val name: String,
    val icon: String,
    val claim: String,
    val intro: String,
    val coverages: List<Pair<String, String>>, // titolo → descrizione
)

data class LegalDoc(
    val eyebrow: String,
    val title: String,
    val updated: String,
    val sections: List<Pair<String, String>>, // heading → paragrafo
)

data class BlogPost(
    val slug: String,
    val date: String,
    val tag: String,
    val title: String,
    val excerpt: String,
    val body: List<String>,
)

object SiteContent {

    val products: Map<String, ProductPage> = listOf(
        ProductPage(
            "auto", "Auto", "🚗",
            claim = "Guida sereno, ci pensiamo noi",
            intro = "La polizza Auto ACME unisce RC obbligatoria e garanzie accessorie modulari, " +
                "con assistenza stradale 24/7 e gestione sinistri interamente digitale.",
            coverages = listOf(
                "RC Auto" to "Copertura fino a 6.000.000 € per i danni causati a terzi.",
                "Furto e Incendio" to "Rimborso del valore commerciale in caso di furto o incendio del veicolo.",
                "Assistenza stradale" to "Soccorso e traino 24/7 in Italia e in Europa.",
                "Tutela legale" to "Spese legali e peritali in caso di controversie.",
            ),
        ),
        ProductPage(
            "casa", "Casa", "🏠",
            claim = "Proteggi ciò che chiami casa",
            intro = "La polizza Casa ACME copre l'abitazione e il suo contenuto da incendio, furto e " +
                "danni accidentali, con la responsabilità civile della famiglia inclusa.",
            coverages = listOf(
                "Incendio e scoppio" to "Ricostruzione del fabbricato fino a 200.000 €.",
                "Furto e rapina" to "Indennizzo per il contenuto sottratto, anche fuori casa.",
                "Responsabilità civile" to "Danni involontari a terzi fino a 1.000.000 €.",
                "Danni da acqua" to "Rotture e infiltrazioni dell'impianto idraulico.",
            ),
        ),
        ProductPage(
            "vita", "Vita", "💙",
            claim = "Un pensiero in meno per chi ami",
            intro = "La polizza Vita ACME mette al sicuro il futuro delle persone che contano, " +
                "unendo protezione del capitale e soluzioni di risparmio.",
            coverages = listOf(
                "Capitale caso morte" to "Liquidazione ai beneficiari fino a 150.000 €.",
                "Invalidità permanente" to "Capitale in caso di invalidità da infortunio o malattia.",
                "Piano di risparmio" to "Accantonamento programmato con rendimento garantito.",
                "Esonero pagamento premi" to "In caso di inabilità, i premi restano a carico nostro.",
            ),
        ),
    ).associateBy { it.key }

    val legal: Map<String, LegalDoc> = mapOf(
        "privacy" to LegalDoc(
            "Informativa", "Privacy", "Aggiornata il 1 giugno 2026",
            listOf(
                "Titolare del trattamento" to "ACME Assicurazioni S.p.A. (POC dimostrativa) è titolare del trattamento dei dati personali raccolti tramite questo sito.",
                "Dati raccolti" to "Raccogliamo solo i dati necessari all'erogazione dei servizi: dati anagrafici, di contatto e relativi alle polizze sottoscritte.",
                "Finalità" to "I dati sono trattati per la gestione del rapporto assicurativo, l'assistenza e, previo consenso, per finalità informative.",
                "Diritti dell'interessato" to "Puoi accedere, rettificare o cancellare i tuoi dati e opporti al trattamento scrivendo a privacy@acme-poc.example.",
            ),
        ),
        "cookie" to LegalDoc(
            "Informativa", "Cookie", "Aggiornata il 1 giugno 2026",
            listOf(
                "Cosa sono i cookie" to "I cookie sono piccoli file di testo che i siti salvano sul dispositivo per funzionare correttamente e raccogliere statistiche.",
                "Cookie tecnici" to "Questa POC usa esclusivamente un cookie di sessione HttpOnly per l'autenticazione all'area personale: non è leggibile da JavaScript.",
                "Cookie di terze parti" to "Non sono installati cookie di profilazione né di terze parti.",
                "Gestione" to "Puoi eliminare i cookie dalle impostazioni del browser; la disattivazione del cookie tecnico impedisce l'accesso all'area riservata.",
            ),
        ),
        "accessibilita" to LegalDoc(
            "Dichiarazione", "Accessibilità", "Aggiornata il 1 giugno 2026",
            listOf(
                "Impegno" to "ACME si impegna a rendere il sito accessibile in conformità alle linee guida WCAG 2.1 livello AA.",
                "Misure adottate" to "HTML semantico, contrasto adeguato, focus sempre visibile da tastiera, target di tocco ≥ 44px, componenti interattivi con ARIA nativo (Web Awesome) e skip-link.",
                "Tecnologie compatibili" to "Il sito è progettato per funzionare con i principali screen reader e con la sola tastiera.",
                "Feedback" to "Segnala eventuali barriere all'accessibilità a accessibilita@acme-poc.example: interverremo quanto prima.",
            ),
        ),
    )

    val blog: List<BlogPost> = listOf(
        BlogPost(
            "rc-auto-cosa-cambia", "12 giugno 2026", "Auto",
            "RC Auto 2026: cosa cambia per chi guida",
            "Nuove regole su scatola nera e sconti: ecco come incidono sul premio.",
            listOf(
                "Dal 2026 l'adozione della scatola nera diventa un fattore sempre più rilevante nel calcolo del premio RC Auto.",
                "Per chi guida poco o in modo prudente, i dispositivi telematici possono tradursi in sconti significativi al rinnovo.",
                "Con ACME puoi attivare la telematica direttamente dall'area personale e monitorare i tuoi vantaggi.",
            ),
        ),
        BlogPost(
            "casa-sicura-estate", "5 giugno 2026", "Casa",
            "Casa sicura prima delle vacanze: la checklist",
            "Cinque accortezze per partire tranquilli e ridurre il rischio di furti e danni.",
            listOf(
                "Prima di partire, verifica la chiusura di porte e finestre e simula una presenza con luci temporizzate.",
                "Controlla l'impianto idraulico: una piccola perdita può trasformarsi in un grande danno se lasciata per settimane.",
                "Con la polizza Casa ACME la copertura furto resta attiva anche quando sei lontano.",
            ),
        ),
        BlogPost(
            "vita-risparmio-giovani", "28 maggio 2026", "Vita",
            "Iniziare presto a risparmiare: perché conviene",
            "Anche piccoli accantonamenti, nel tempo, fanno una grande differenza.",
            listOf(
                "Il tempo è l'alleato più potente del risparmio: iniziare a vent'anni vale molto più che raddoppiare gli sforzi a quaranta.",
                "Le soluzioni Vita ACME permettono versamenti flessibili, adattabili alle fasi della vita.",
                "Parla con un agente per costruire un piano su misura, senza vincoli rigidi.",
            ),
        ),
    )
}
