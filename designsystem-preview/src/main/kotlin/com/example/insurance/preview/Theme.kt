package com.example.insurance.preview

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.insurance.shared.designsystem.Tokens

/** Stessi token di brand condivisi (:shared) -> coerenza con web e Android. */
private fun hex(s: String) = Color(("ff" + s.removePrefix("#")).toLong(16))

private val Scheme = lightColorScheme(
    primary = hex(Tokens.Color.Primary),
    secondary = hex(Tokens.Color.Accent),
    background = hex(Tokens.Color.Background),
    surface = hex(Tokens.Color.Surface),
    onPrimary = Color.White,
)

@Composable
fun InsuranceTheme(content: @Composable () -> Unit) =
    MaterialTheme(colorScheme = Scheme, content = content)
