# Insurance POC — Avvio rapido (senza Strapi)

POC assicurativa **Kotlin Multiplatform**: sito web (Compose HTML/JS), 2 backend Ktor e app Android.
Questo bundle **non include Strapi**: i testi istituzionali arrivano da un **contenuto mock** del BFF,
mentre polizze/sinistri/agenzie dal backend fittizio `policy-api`. Niente da configurare.

## Cosa serve
- **JDK 17+** (per i comandi `./gradlew` da terminale). Verifica: `java -version`.
- **Android Studio** (recente, con **SDK Platform 36**) per l'app Android.
- **Kobweb CLI** per il sito web → installa una volta:
  - macOS: `brew install varabyte/tap/kobweb`
  - altri OS: vedi https://kobweb.varabyte.com (sezione *Install the Kobweb binary*).
- VS Code va benissimo per leggere/modificare il codice (è solo un editor; build e run si fanno con Gradle/Android Studio).

> Non serve IntelliJ. Il modulo `:designsystem-preview` (anteprime in IntelliJ) **non è incluso**:
> su Android Studio le anteprime dei componenti sono i `@Preview` in `androidApp/.../ui/ComponentPreviews.kt`.

## 1) Backend (2 terminali)
Dalla cartella del progetto:

```bash
# Terminale A — backend fittizio polizze/sinistri/agenzie  → http://localhost:9000
./gradlew :policy-api:run

# Terminale B — BFF (unica porta verso CMS e API)           → http://localhost:8080
./gradlew :bff:run
```
Nessuna variabile d'ambiente: il BFF punta da solo a `policy-api` su `:9000` e, senza Strapi,
serve i testi istituzionali **mock**.
(Windows: usa `gradlew.bat` al posto di `./gradlew`.)

## 2) Sito web (1 terminale) → http://localhost:8081
```bash
cd web
kobweb run
```
Apri **http://localhost:8081**. Login POC: **qualsiasi** username/password non vuoti.
Rotte utili: `/`, `/prodotti`, `/prodotto?key=auto`, `/trova-agenzia`, `/blog`, `/contatti`,
`/design-system`, `/login`, `/area/policies`.

> Avvia prima i backend (passo 1): l'area personale e lo store locator leggono i dati dal BFF.

## 3) App Android
1. In Android Studio: **Open** → seleziona la cartella del progetto e attendi il *Gradle sync*
   (genera da solo `local.properties` con il path dell'SDK; se chiede, installa **SDK Platform 36**).
2. Avvia un **emulatore**.
3. **Tieni accesi i backend** del passo 1: l'app chiama il BFF su `http://10.0.2.2:8080`
   (alias dell'host dall'emulatore) → quindi BFF su `:8080` e policy-api su `:9000`.
4. Esegui la configurazione **androidApp**. Login uguale al web.
5. Anteprime componenti: apri `androidApp/.../ui/ComponentPreviews.kt` → vista **Split/Design**.

## Architettura (sintesi)
```
[Web · Compose HTML] ─┐                       ┌─► (Strapi, NON in questo bundle → contenuti mock)
   cookie HttpOnly    ├─ HTTP ─► [BFF · Ktor] ─┤
[Android · Compose]  ─┘  Bearer   auth+proxy    └─► policy-api (polizze/sinistri/agenzie, :9000)
```
- I client non chiamano mai i backend a valle: passano sempre dal **BFF** (s2s).
- Auth: web = JWT in cookie HttpOnly; Android = JWT Bearer.
- UI riusabile nel modulo **`:designsystem`** (CSS in chunk via StyleSheet Compose; vedi README).

## Problemi comuni
- **`./gradlew` usa un JDK sbagliato**: imposta `JAVA_HOME` su un JDK 17+ (oppure usa il terminale di Android Studio, che eredita il JDK incluso).
- **L'area personale è vuota / errori**: assicurati che `policy-api` (:9000) e `bff` (:8080) siano avviati prima del web/app.
- **`kobweb: command not found`**: installa la Kobweb CLI (vedi sopra).
