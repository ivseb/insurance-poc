# Blue-Green sullo stack applicativo (Argo Rollouts)

Deploy blue-green **full-stack** di `insurance-poc`: una nuova versione entra in un ambiente **preview
(green)** isolato — `web-preview` + `bff-preview` che parlano tra loro — mentre la **produzione (blue)**
resta intatta. Dopo le verifiche sul preview, **promuovi** e il traffico passa a green. Se qualcosa non va,
rollback immediato.

```
   PRODUZIONE (blue)                          PREVIEW (green)
   insurance.127.0.0.1.nip.io                 preview.insurance.127.0.0.1.nip.io
        │                                          │
   ingress /  → web   (active) ─┐            ingress /  → web-preview  ─┐
   ingress /api → bff (active) ─┴─► policy-api ◄─┴─ ingress /api → bff-preview
```

`policy-api` è il core condiviso (una sola versione): la "feature" vive in web+bff.

## Come funziona (dov'è nel repo)

- I `Deployment` di **web** e **bff** sono diventati `Rollout` blue-green nella chart Helm
  ([`chart/templates/web.yaml`](../../chart/templates/web.yaml),
  [`chart/templates/bff.yaml`](../../chart/templates/bff.yaml)),
  con `autoPromotionEnabled: false` → **promozione manuale**.
- Ogni Rollout ha due Service: `web`/`bff` (**active** = produzione) e `web-preview`/`bff-preview`
  (**preview** = green durante un rilascio, altrimenti = stabile).
- Il routing `/api` è stato spostato **sull'ingress per-host** ([`chart/templates/ingress.yaml`](../../chart/templates/ingress.yaml)):
  così il web-green del preview parla col bff-green del preview, senza cambiare l'immagine web.
- L'`Application` ArgoCD ignora il selector iniettato dal controller
  ([`deploy/argocd/apps/insurance-poc.yaml`](../argocd/apps/insurance-poc.yaml) → `ignoreDifferences`),
  così `selfHeal` non litiga col controller.

## Il flusso reale (verifica → promozione)

1. **Merghi una feature** (web e/o bff) su `main`. La CI costruisce le nuove immagini, bump-a i tag su
   `env/prod`; ArgoCD sincronizza.
2. Il Rollout vede la nuova immagine → avvia il **green in preview** e va in **pausa** (`autoPromotion=false`).
   **La produzione (blue) non cambia.**
3. Apri il **preview** e collaudi la feature end-to-end:
   > **http://preview.insurance.127.0.0.1.nip.io**

   (login, giro polizze, ecc. — il preview usa web-green + bff-green; policy-api è quello condiviso.)
4. Se ok, **promuovi** (traffico blue→green istantaneo). Se no, **abort** (nessun impatto su prod).

### Comandi (CLI)

```bash
# stato live dei due rollout
kubectl argo rollouts get rollout bff -n insurance-poc --watch
kubectl argo rollouts get rollout web -n insurance-poc --watch

# promozione (feature full-stack: promuovi entrambi)
kubectl argo rollouts promote bff -n insurance-poc
kubectl argo rollouts promote web -n insurance-poc

# rollback
kubectl argo rollouts abort <bff|web> -n insurance-poc      # durante la pausa (green non ancora promosso)
kubectl argo rollouts undo  <bff|web> -n insurance-poc      # dopo la promozione (torna al blu)
```

### Dashboard

La dashboard Argo Rollouts è montata in-cluster ([`dashboard.yaml`](dashboard.yaml)) ed esposta su:

> **http://rollouts.127.0.0.1.nip.io** → seleziona il namespace **`insurance-poc`**

Da lì vedi i due Rollout e puoi fare **Promote / Abort / Rollback** coi pulsanti.

## Note

- **Solo web cambia** → parte solo il green del web; `bff-preview` punta al bff stabile → il preview è
  comunque coerente (web-green + bff-stabile). Idem se cambia solo il bff. Se cambiano entrambi → green+green.
- **Promozione full-stack:** promuovi prima `bff` poi `web` (o viceversa: hai già validato il preview).
- Il **controller Argo Rollouts** e la **dashboard** sono infrastruttura di bootstrap (come ingress-nginx e
  ArgoCD stesso), installati una-tantum:
  ```bash
  kubectl create namespace argo-rollouts
  kubectl apply -n argo-rollouts -f https://github.com/argoproj/argo-rollouts/releases/download/v1.9.0/install.yaml
  kubectl apply -f deploy/argo-rollouts/dashboard.yaml
  # plugin CLI:
  brew install argoproj/tap/kubectl-argo-rollouts   # oppure il binario dalla release
  ```
