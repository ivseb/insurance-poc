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
- `web` (nginx, ClusterIP) — serve l'export statico Kobweb e fa da proxy `/api → bff`
  (stessa origin → cookie HttpOnly ok, niente CORS).
- **ingress-nginx** espone tutto su `localhost:80` via host **nip.io** (nessun `/etc/hosts`):
  - app → `http://insurance.127.0.0.1.nip.io`
  - dashboard ArgoCD → `http://argocd.127.0.0.1.nip.io`

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

### 3) Ingress controller (una tantum)
```bash
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/controller-v1.11.3/deploy/static/provider/cloud/deploy.yaml
kubectl -n ingress-nginx rollout status deploy/ingress-nginx-controller
```
Su Docker Desktop il controller prende `localhost:80`. Gli host `*.127.0.0.1.nip.io` risolvono da
soli a `127.0.0.1`: **nessun `/etc/hosts`**.

### 4) Installa ArgoCD, esponilo e crea l'Application
```bash
kubectl create namespace argocd
kubectl apply -n argocd -f https://raw.githubusercontent.com/argoproj/argo-cd/stable/manifests/install.yaml
kubectl -n argocd rollout status deploy/argocd-server

# dashboard via ingress (http): metti il server in insecure + Ingress
kubectl -n argocd patch configmap argocd-cmd-params-cm --type merge -p '{"data":{"server.insecure":"true"}}'
kubectl -n argocd rollout restart deploy/argocd-server
kubectl apply -f deploy/argocd/argocd-ingress.yaml

# collega ArgoCD a questo repo/manifest
kubectl apply -f deploy/argocd/application.yaml
```

### 5) Accesso
- **App**: http://insurance.127.0.0.1.nip.io  (login: qualsiasi username/password non vuoti).
- **Dashboard ArgoCD**: http://argocd.127.0.0.1.nip.io
  ```bash
  # user: admin
  kubectl -n argocd get secret argocd-initial-admin-secret -o jsonpath='{.data.password}' | base64 -d; echo
  ```

## Come funziona a regime
Modifichi il codice → push su `main` → la CI ricostruisce e pubblica → committa i nuovi tag →
ArgoCD sincronizza → il cluster fa il rollout. Nessun `kubectl apply` manuale.

## Troubleshooting
- **Pod in `ImagePullBackOff`**: le immagini GHCR non sono pubbliche (vedi passo 2) oppure la CI
  non ha ancora pubblicato.
- **`http://localhost` non funziona come atteso**: usa `http://insurance.127.0.0.1.nip.io` (passo 5).
- **Stato deploy**:
  ```bash
  kubectl -n insurance-poc get pods,svc
  kubectl -n argocd get applications
  ```
