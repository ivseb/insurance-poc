package com.example.insurance.shared.designsystem

/**
 * Design token neutri (stile moderno), definiti UNA volta e mappati per piattaforma:
 *  - web    -> variabili CSS (vedi web/.../Theme.kt)
 *  - Android -> Compose Color/Dp (vedi androidApp/.../Theme.kt)
 * Niente dipendenze grafiche qui: solo valori. Così il brand non diverge tra canali.
 */
object Tokens {
    object Color {
        const val Primary = "#00008F"      // ACME blue
        const val PrimaryDark = "#00006B"
        const val Accent = "#FF1721"       // ACME red
        const val Ink = "#1A1A2E"
        const val Muted = "#6B7280"
        const val Surface = "#FFFFFF"
        const val Background = "#F5F6FA"
        const val Success = "#1E7B34"
        const val Warning = "#B25E00"
    }

    /** Spaziatura in px (scala 4pt). */
    object Space {
        const val Xs = 4
        const val Sm = 8
        const val Md = 16
        const val Lg = 24
        const val Xl = 40
    }

    object Radius { const val Md = 8; const val Lg = 16 }

    object Font {
        const val Family = "'Source Sans 3', system-ui, -apple-system, sans-serif"
        const val SizeBody = 16
        const val SizeH1 = 40
        const val SizeH2 = 28
    }
}
