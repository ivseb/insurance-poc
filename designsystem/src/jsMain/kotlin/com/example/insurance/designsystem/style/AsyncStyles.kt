package com.example.insurance.designsystem.style

import org.jetbrains.compose.web.css.*

/** Chunk ASYNC: skeleton "shimmer" per porzioni caricate lazy (keyframes in base.css). */
object AsyncStyles : StyleSheet() {
    val card by style {
        property("background", "var(--surface)"); property("border", "1px solid var(--line)")
        property("border-radius", "var(--radius-lg)"); property("padding", "var(--sp-5)"); property("min-height", "132px")
    }
    val line by style {
        property("height", "14px"); property("border-radius", "6px"); property("margin-bottom", "var(--sp-3)")
        property("background", "linear-gradient(90deg, var(--bg) 25%, #E6E6EF 37%, var(--bg) 63%)")
        property("background-size", "200% 100%"); property("animation", "skeleton-shimmer 1.3s ease-in-out infinite")
    }
    val lineTitle by style { property("height", "20px"); property("width", "55%") }
    val lineShort by style { property("width", "40%"); property("margin-bottom", "0") }
}
