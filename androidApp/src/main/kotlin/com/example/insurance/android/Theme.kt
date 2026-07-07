package com.example.insurance.android

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight

/**
 * Brand ACME su Android (allineato al web e ai siti reali un design moderno / areaclienti):
 * navy + rosso, sfondo grigio, titoli SERIF (FontFamily.Serif evoca Publico Headline,
 * a pagamento), card polizza verdi. I valori rispecchiano i token condivisi (:shared).
 */
object BrandColors {
    val Navy = Color(0xFF00008F)
    val NavyDark = Color(0xFF00006B)
    val NavyTint = Color(0xFFE9E9F6)
    val Red = Color(0xFFFF1721)
    val Ink = Color(0xFF282832)
    val Slate = Color(0xFF5A6872)
    val Line = Color(0xFFE2E2EA)
    val Bg = Color(0xFFF1F1F1)
    val Green = Color(0xFF1E7B47)
    val GreenBtn = Color(0xFF7FB893)
    val GreenTint = Color(0xFFE8F3EC)
    val GreenLine = Color(0xFFCDE6D5)
    val Amber = Color(0xFFB25E00)
}

private val Scheme = lightColorScheme(
    primary = BrandColors.Navy,
    onPrimary = Color.White,
    primaryContainer = BrandColors.NavyTint,
    onPrimaryContainer = BrandColors.Navy,
    secondary = BrandColors.Red,
    onSecondary = Color.White,
    error = BrandColors.Red,
    onError = Color.White,
    background = BrandColors.Bg,
    onBackground = BrandColors.Ink,
    surface = Color.White,
    onSurface = BrandColors.Ink,
    surfaceVariant = BrandColors.GreenTint,
    onSurfaceVariant = BrandColors.Ink,
    outline = BrandColors.Line,
)

/** Titoli serif (firma ACME); body resta sans di sistema. */
private val AppTypography = Typography().run {
    val serif = FontFamily.Serif
    copy(
        headlineLarge = headlineLarge.copy(fontFamily = serif, fontWeight = FontWeight.Bold),
        headlineMedium = headlineMedium.copy(fontFamily = serif, fontWeight = FontWeight.Bold),
        headlineSmall = headlineSmall.copy(fontFamily = serif, fontWeight = FontWeight.Bold),
        titleLarge = titleLarge.copy(fontFamily = serif, fontWeight = FontWeight.Bold),
    )
}

@Composable
fun InsuranceTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = Scheme, typography = AppTypography, content = content)
}
