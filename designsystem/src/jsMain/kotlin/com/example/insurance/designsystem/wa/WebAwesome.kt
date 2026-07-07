package com.example.insurance.designsystem.wa

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.dom.TagElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event

/**
 * Interop sottile tra Compose HTML e i Web Components di Web Awesome (<wa-*>).
 * L'accessibilità (ARIA, focus, tastiera) è dentro i componenti della libreria.
 * Nessun import Kobweb qui: resta riusabile.
 */
private fun dynValue(e: Event): String = (e.target.asDynamic().value as? String) ?: ""

/** <wa-button>. In un <form>, type="submit" invia (i bottoni WA sono form-associated). */
@Composable
fun WaButton(
    variant: String = "brand",
    appearance: String = "accent",
    type: String? = null,
    size: String? = null,
    pill: Boolean = false,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    TagElement<HTMLElement>("wa-button", applyAttrs = {
        attr("variant", variant)
        attr("appearance", appearance)
        type?.let { attr("type", it) }
        size?.let { attr("size", it) }
        if (pill) attr("pill", "")
        onClick?.let { cb ->
            ref { el ->
                val h: (Event) -> Unit = { cb() }
                el.addEventListener("click", h)
                onDispose { el.removeEventListener("click", h) }
            }
        }
    }) { content() }
}

/** <wa-input> non controllato: valore iniziale + onInput -> stato Compose (no cursor jump). */
@Composable
fun WaInput(
    label: String,
    type: String = "text",
    id: String? = null,
    autocomplete: String? = null,
    required: Boolean = false,
    hint: String? = null,
    onInput: (String) -> Unit,
) {
    TagElement<HTMLElement>("wa-input", applyAttrs = {
        attr("label", label)
        attr("type", type)
        id?.let { attr("id", it) }
        autocomplete?.let { attr("autocomplete", it) }
        if (required) attr("required", "")
        hint?.let { attr("hint", it) }
        ref { el ->
            val h: (Event) -> Unit = { onInput(dynValue(it)) }
            el.addEventListener("input", h)
            onDispose { el.removeEventListener("input", h) }
        }
    }, content = null)
}

/** <wa-textarea>. */
@Composable
fun WaTextarea(
    label: String,
    id: String? = null,
    rows: Int = 4,
    hint: String? = null,
    onInput: (String) -> Unit,
) {
    TagElement<HTMLElement>("wa-textarea", applyAttrs = {
        attr("label", label)
        attr("rows", rows.toString())
        id?.let { attr("id", it) }
        hint?.let { attr("hint", it) }
        ref { el ->
            val h: (Event) -> Unit = { onInput(dynValue(it)) }
            el.addEventListener("input", h)
            onDispose { el.removeEventListener("input", h) }
        }
    }, content = null)
}

/** <wa-badge variant=...>. */
@Composable
fun WaBadge(variant: String, content: @Composable () -> Unit) {
    TagElement<HTMLElement>("wa-badge", applyAttrs = {
        attr("variant", variant)
        attr("appearance", "filled")
    }) { content() }
}

/** <wa-callout variant=...> per messaggi (success/danger/neutral). */
@Composable
fun WaCallout(variant: String, content: @Composable () -> Unit) {
    TagElement<HTMLElement>("wa-callout", applyAttrs = { attr("variant", variant) }) { content() }
}
