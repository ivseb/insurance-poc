# CI/CD & Osservabilità — GitHub Actions → GHCR → ArgoCD (Helm) su k8s locale

Pipeline **GitOps**: si lavora in **PR** (main protetto), al merge la CI builda le immagini con
**versione semantica**, aggiorna i tag nel **chart Helm** su un branch di deploy `env/prod`, e
**ArgoCD** lo sincronizza sul cluster.

```
PR ─(check verde)─► merge su main ─► GitHub Actions
     │                                   ├─ semver (conventional-commits) → vX.Y.Z
     │                                   ├─ build parallela (solo i moduli cambiati) → ghcr.io/ivseb/insurance-poc-*:X.Y.Z
     │                                   ├─ git tag vX.Y.Z
     │                                   └─ aggiorna chart/values.yaml (ACCUMULO tag) → push su env/prod
main PROTETTO (solo PR)                                     │
                                        ArgoCD (watch env/prod, path=chart Helm) ─► cluster
```

**Perché `env/prod`:** `main` è protetto (solo PR), quindi la CI **non** ci scrive; i tag di deploy
vivono su `env/prod` (branch che ArgoCD segue). Nessun bypass del bot.

## Runtime (namespace `insurance-poc`)
- `policy-api` (ClusterIP :9000) — backend fittizio polizze/sinistri/agenzie.
- `bff` (ClusterIP :8080) — `POLICY_API_URL=http://policy-api:9000`, niente Strapi → CMS mock.
- `web` (nginx, ClusterIP) — export statico Kobweb + proxy `/api → bff` (stessa origin → cookie HttpOnly, niente CORS).
- **ingress-nginx** su `localhost:80` con host **nip.io** (nessun `/etc/hosts`):
  - app → `http://insurance.127.0.0.1.nip.io`
  - ArgoCD → `http://argocd.127.0.0.1.nip.io`
  - Grafana → `http://grafana.127.0.0.1.nip.io`
- **Versione a runtime**: `GET /api/version` → `{"bff":"X.Y.Z","policyApi":"X.Y.Z"}` · `GET /version.txt` (web).

## Osservabilità (namespace `observability`)
Stack **`grafana/otel-lgtm`** (OTel Collector + Prometheus + Loki + Tempo + Grafana, datasource cablati).
Instrumentation **nativa**:
- **OTel Java agent** su bff/policy-api → Ktor server+client, Netty, JVM, logback (trace + metriche + log OTLP).
- **OTel nativo su ingress-nginx** → span edge, propagati → **trace distribuita** edge → bff → policy-api.
- **kube-state-metrics** (via Prometheus) per restart pod / risorse.

## Notifiche & rete di sicurezza
- **ArgoCD Notifications**: trigger `on-health-degraded` e `on-deployed` → webhook (POC: ricevitore echo; in prod → Slack/email).
- **Drift-check** (workflow schedulato): per ogni modulo confronta l'ultimo commit su `main` con la
  **versione** deployata su `env/prod` (versione → git tag → commit → controllo antenato). Rosso se
  qualcosa non è ancora rilasciato.

---

## Setup una-tantum (Docker Desktop k8s: context `docker-desktop`; servono `kubectl`, `git`, `gh`)

### 1) Repo + protezione di `main`
```bash
git init && git add . && git commit -m "init"
gh repo create insurance-poc --public --source=. --remote=origin --push
# main: richiede PR + check verde
gh api -X PUT /repos/<owner>/insurance-poc/branches/main/protection --input - <<'JSON'
{ "required_status_checks": {"strict": true, "contexts": ["check"]},
  "enforce_admins": false,
  "required_pull_request_reviews": {"required_approving_review_count": 0},
  "restrictions": null }
JSON
```
Dopo la 1ª CI, rendi **pubblici** i 3 package GHCR (GitHub → Packages → *Change visibility → Public*),
così il cluster li scarica senza secret.

### 2) Ingress controller
```bash
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/controller-v1.11.3/deploy/static/provider/cloud/deploy.yaml
kubectl -n ingress-nginx rollout status deploy/ingress-nginx-controller
# OTel nativo sull'ingress (span edge → trace distribuite)
kubectl -n ingress-nginx patch configmap ingress-nginx-controller --type merge \
  -p '{"data":{"enable-opentelemetry":"true","otlp-collector-host":"otel-lgtm.observability","otlp-collector-port":"4317","otel-service-name":"ingress-nginx","opentelemetry-trust-incoming-span":"true"}}'
kubectl -n ingress-nginx rollout restart deploy/ingress-nginx-controller
```

### 3) ArgoCD (con dashboard via ingress)
```bash
kubectl create namespace argocd
kubectl apply -n argocd -f https://raw.githubusercontent.com/argoproj/argo-cd/stable/manifests/install.yaml
kubectl -n argocd rollout status deploy/argocd-server
kubectl -n argocd patch configmap argocd-cmd-params-cm --type merge -p '{"data":{"server.insecure":"true"}}'
kubectl -n argocd rollout restart deploy/argocd-server
kubectl apply -f deploy/argocd/argocd-ingress.yaml
```

### 4) Bootstrap **app-of-apps** (un solo comando → tutto GitOps)
```bash
kubectl apply -f deploy/argocd/root.yaml
```
`root` gestisce le Application figlie in `deploy/argocd/apps/`: **insurance-poc** (app, `env/prod`+chart),
**monitoring** (otel-lgtm) e **notifications** (config ArgoCD Notifications). Osservabilità e notifiche
sono così **dichiarative + self-healing** come l'app (le sottoscrizioni notifiche sono nell'Application).
L'unica cosa imperativa resta l'installazione di ArgoCD stesso (bootstrap).

### 5) Accesso
| Cosa | URL | Credenziali |
|---|---|---|
| App | http://insurance.127.0.0.1.nip.io | login: qualsiasi user/password non vuoti |
| ArgoCD | http://argocd.127.0.0.1.nip.io | `admin` · `kubectl -n argocd get secret argocd-initial-admin-secret -o jsonpath='{.data.password}' \| base64 -d` |
| Grafana | http://grafana.127.0.0.1.nip.io | anonimo (Admin) |

## Come funziona a regime
1. Branch → PR → il job **`check`** compila (deve essere verde) → **merge** su `main`.
2. La CI calcola la **versione semantica** (feat→minor, `BREAKING`/`!`→major, altro→patch), builda **solo
   i moduli cambiati**, pubblica `…:X.Y.Z`, crea il git tag `vX.Y.Z`, aggiorna i tag su `env/prod` (accumulo).
3. **ArgoCD** sincronizza il chart → rollout. Nessun `kubectl apply` manuale.

## Troubleshooting
- **Pod `ImagePullBackOff`** → package GHCR non pubblici (passo 1) o CI non ancora finita.
- **La PR non si può mergiare / `check` non parte** su una PR **solo-documentazione**: il workflow ha
  `paths-ignore: '**.md'` anche su `pull_request`, quindi per i doc il check richiesto non gira. Rimedi:
  fai un commit non-doc nella stessa PR, oppure togli `paths-ignore` dal trigger `pull_request`.
- **Stato**:
  ```bash
  kubectl -n insurance-poc get pods,svc
  kubectl -n argocd get applications
  git show origin/env/prod:chart/values.yaml | grep tag   # versioni deployate
  ```
