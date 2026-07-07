# CI/CD — GitHub Actions → GHCR → ArgoCD (k8s locale su Docker Desktop)

Pipeline GitOps: a ogni push su `main`, GitHub Actions builda le immagini, le pubblica su GHCR,
aggiorna i tag nei manifest e li committa; **ArgoCD** vede il commit e sincronizza il cluster.

```
push main ─► GitHub Actions ─► ghcr.io/ivseb/insurance-poc-{policy-api,bff,web}:<sha>
                     └► sed newTag in deploy/kustomization.yaml + commit [skip ci]
                                              │
                                    ArgoCD (watch deploy/) ─► kubectl apply ─► cluster
```

**Runtime nel cluster** (namespace `insurance-poc`):
- `policy-api` (ClusterIP :9000) — backend fittizio polizze/sinistri/agenzie.
- `bff` (ClusterIP :8080) — `POLICY_API_URL=http://policy-api:9000`, nessuno Strapi → CMS mock.
- `web` (nginx, LoadBalancer :80) — serve l'export statico Kobweb e fa da proxy `/api → bff`
  (stessa origin → cookie HttpOnly ok, niente CORS).

## Prerequisiti (una tantum, sulla tua macchina)
- Docker Desktop con **Kubernetes attivo** (context `docker-desktop`).
- `kubectl`, `git`, `gh` (autenticato).

### 1) Repo GitHub
```bash
git init && git add . && git commit -m "init"
gh repo create insurance-poc --public --source=. --remote=origin --push
```
Il push su `main` fa partire la CI.

### 2) Rendi PUBBLICHE le 3 immagini GHCR (una tantom, dopo la prima CI)
I package GHCR nascono privati. Per farli scaricare al cluster senza secret:
GitHub → il tuo profilo → **Packages** → per ognuna di `insurance-poc-policy-api|bff|web`
→ *Package settings* → **Change visibility** → *Public*.
(In alternativa a immagini private: crea un `imagePullSecret` nel namespace e referenzialo nei Deployment.)

### 3) Installa ArgoCD e crea l'Application
```bash
kubectl create namespace argocd
kubectl apply -n argocd -f https://raw.githubusercontent.com/argoproj/argo-cd/stable/manifests/install.yaml
kubectl -n argocd rollout status deploy/argocd-server

# collega ArgoCD a questo repo/manifest
kubectl apply -f deploy/argocd/application.yaml
```
ArgoCD (auto-sync + self-heal) applicherà `deploy/` appena le immagini esistono.

### 4) Accesso al sito
Il frontend deve essere raggiunto con un host **diverso da `localhost`** (così usa la stessa origin
del BFF via nginx). Aggiungi a `/etc/hosts`:
```
127.0.0.1   insurance.local
```
Poi apri **http://insurance.local**. Login: qualsiasi username/password non vuoti.

## Come funziona a regime
- Modifichi il codice → push su `main` → la CI ricostruisce e pubblica → committa i nuovi tag →
  ArgoCD sincronizza → il cluster fa il rollout. Nessun `kubectl apply` manuale.
- Console ArgoCD (opzionale):
  ```bash
  kubectl -n argocd port-forward svc/argocd-server 8090:443
  # user: admin  pass: kubectl -n argocd get secret argocd-initial-admin-secret -o jsonpath='{.data.password}' | base64 -d
  ```

## Troubleshooting
- **Pod in `ImagePullBackOff`**: le immagini GHCR non sono pubbliche (vedi passo 2) oppure la CI
  non ha ancora pubblicato.
- **`http://localhost` non funziona come atteso**: usa `http://insurance.local` (passo 4).
- **Stato deploy**:
  ```bash
  kubectl -n insurance-poc get pods,svc
  kubectl -n argocd get applications
  ```
