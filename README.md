# Insurance POC — Kotlin Multiplatform (Compose HTML + Android) con BFF

POC assicurativa: **logica condivisa**, **UI per canale**, **design system in modulo separato**.
Web in **Compose HTML** (Kotlin/JS, DOM reale, accessibile) con componenti **Web Awesome**;
Android in **Compose**; un **BFF Ktor** come unica porta verso CMS e backend assicurativo (tutto **s2s**).

> ✅ **Verificato end-to-end**: web in browser headless (istituzionale da Strapi, login via cookie,
> polizze/sinistro dal backend fittizio, gallery, responsive) e Android su emulatore.

## Architettura

```
[Web · Compose HTML/Kobweb] ─┐                         ┌─► Strapi (CMS: solo testi/immagini)
   cookie HttpOnly           ├─ HTTPS ─► [BFF · Ktor] ─┤   (s2s)
[Android · Compose]         ─┘   Bearer    auth+proxy   └─► policy-api (backend fittizio polizze/sinistri)
```

- **CMS↔sito solo s2s**: browser/app non chiamano mai Strapi o policy-api; passano dal BFF.
- **Auth**: web = JWT in **cookie HttpOnly/SameSite** (non leggibile dal JS → no furto via XSS);
  Android = JWT in header **Bearer**. Il BFF accetta entrambi. Security headers (`X-Frame-Options`,
  `X-Content-Type-Options`, `Referrer-Policy`). Token upstream solo nelle env del BFF.

### Moduli
| Modulo | Tech | Ruolo |
|---|---|---|
| `shared` | KMP (js/android/jvm) | DTO, **validazione sinistro**, `BffClient` (android), token di brand |
| `designsystem` | Kobweb library | **Componenti UI riusabili** (Compose HTML + Web Awesome): il design system |
| `policy-api` | Ktor/JVM | **Backend fittizio** che simula l'API assicurativa (polizze/sinistri) |
| `bff` | Ktor/JVM | Backend-for-frontend: auth (cookie+Bearer), proxy **s2s** verso Strapi e policy-api |
| `web` | Kobweb / Compose HTML | App: istituzionale (CMS) + area personale + **gallery `/design-system`** |
| `androidApp` | Compose | Stesse feature su mobile (riusa `shared`) |
| `infra/strapi` | Strapi v5 | CMS reale per i contenuti istituzionali (schema-as-code) |

### Accessibilità (risolta dalla libreria)
I componenti interattivi sono **Web Awesome** (`<wa-button>`, `<wa-input>`, `<wa-badge>`, `<wa-callout>`…):
ARIA, focus e tastiera sono dentro i web component. Tematizzati al brand ACME via i token `--wa-*`.
HTML semantico, skip-link, `:focus-visible`, target ≥ 44px. Layout **responsive** (nav mobile, griglie fluide).

### Anteprima dei componenti (`@Preview`)
- **IntelliJ IDEA** → modulo **`:designsystem-preview`** (Compose **Desktop**): apri
  `designsystem-preview/.../Previews.kt` e usa il pannello Preview. Richiede il plugin IntelliJ
  **"Compose Multiplatform IDE Support"**. In più è avviabile come gallery: `./gradlew :designsystem-preview:run`.
- **Android Studio** → veri `@Preview` in `androidApp/.../ui/ComponentPreviews.kt` (gli stessi componenti
  di `MainActivity`).
- **Web (Compose HTML)** → `@Preview` non esiste per Kotlin/JS: l'equivalente è la **gallery live su
  `/design-system`**.

> I `@Preview` Desktop/Android renderizzano composable **Material3** (Android/Desktop). I componenti **web**
> sono Compose **HTML** e non sono renderizzabili fuori dal browser: il modulo Desktop ne rispecchia il
> design usando gli **stessi token di brand** (`:shared`) per restare coerente.

### Isolamento di Kobweb (scelta dell'utente)
I componenti del design system non importano Kobweb: la navigazione passa per `LocalNavigate`
(CompositionLocal) fornito dai *page-adapter* lato Kobweb. Sostituire Kobweb = cambiare solo gli adapter.

### CSS in chunk (StyleSheet tipizzati) — niente uber-css
Grafica allineata ad **un design moderno** (istituzionale) e **un'area clienti moderna** (area riservata):
titoli **serif** (Source Serif 4, sostituto libero di Publico Headline), body Source Sans 3, navy
`#00008F`, hero a immagine con card in overlay, bottoni a pillola, nav flottante, card polizza verdi.

I CSS sono organizzati come fanno i framework moderni (chunk per route), **non** in un unico file:
- **`web/.../resources/public/base.css`** — unico CSS globale: solo token (`:root`), reset, focus,
  tematizzazione `--wa-*` e keyframes. È il layer "critico" minimo.
- **StyleSheet Compose tipizzati** (`designsystem/.../style/*.kt`, `web/.../ui/PageStyles.kt`):
  `LayoutStyles`, `ChromeStyles`, `HeroStyles`, `ProductStyles`, `AsyncStyles`, `AreaStyles`, `PageStyles`.
  Ogni componente/guscio monta il proprio chunk con `Style(Sheet)`: Compose HTML inietta il `<style>`
  **solo quando il componente/route è in composizione** → code-splitting nativo per route, type-safe,
  refactorabile. Con l'export statico ogni pagina pre-renderizzata porta **solo i suoi chunk** di CSS.

## Come si esegue

```bash
JDK=17  # i build Gradle usano JDK 17 (pin in gradle.properties)

# 1) CMS Strapi (contenuti istituzionali)  -> http://localhost:1337/admin
cd infra/strapi && npm install && npm run develop      # primo avvio crea schema + seed

# 2) Backend fittizio polizze              -> http://localhost:9000
./gradlew :policy-api:run

# 3) BFF (collega CMS + policy-api)         -> http://localhost:8080
#    Strapi richiede un API token (creato dal bootstrap in infra/strapi/.api-token)
STRAPI_URL=http://localhost:1337 STRAPI_TOKEN=$(cat infra/strapi/.api-token) \
  POLICY_API_URL=http://localhost:9000 ./gradlew :bff:run

# 4) Web (Compose HTML)                     -> http://localhost:8081
cd web && kobweb run
#   export statico:  kobweb export --layout static   (HTML pre-renderizzato in web/.kobweb/site)

# 5) Android
./gradlew :androidApp:assembleDebug
adb install -r androidApp/build/outputs/apk/debug/androidApp-debug.apk
```
Login POC: qualsiasi `username`/`password` non vuoti. Rotte: `/`, `/prodotti`, `/chi-siamo`,
`/design-system`, `/login`, `/area/policies`.

## Strapi — solo testi/immagini, promozione in CI
- Content-type **schema-as-code** in `infra/strapi/src/api/homepage` e `src/components/institutional`
  → versionati in Git, migrano da soli a ogni ambiente. Editing per **utenti business** (admin visuale).
- Promozione contenuti tra istanze dedicate: `strapi transfer` / export-import, invocabili da CI.
- L'endpoint **non è pubblico**: il bootstrap crea un **API token full-access "bff"** (accessKey in
  `infra/strapi/.api-token`) e revoca la lettura pubblica. Il BFF lo usa per le chiamate s2s (il token
  vive solo nelle env del BFF); mappa poi la forma Strapi → DTO condivisi.

## Toolchain
Font: Source Sans 3 (body) + Source Serif 4 (titoli). 
Gradle 9.3.1 · Kotlin 2.3.20 · Kobweb 0.24.0 · Compose HTML 1.10.0 · Web Awesome 3.9.0 ·
Ktor 3.5.0 · AGP 8.13.2 · Compose BOM 2026.06.00 · Strapi 5.48 · JDK 17 per i build.

### Nota: shim `requestAnimationFrame`
`web/build.gradle.kts` inietta uno shim rAF attivo **solo se `document.hidden`** (tab headless):
serve a far girare gli effetti Compose nei test headless; in un browser visibile è un no-op.
