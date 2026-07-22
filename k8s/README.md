# Kubernetes deployment (k3s)

Manifests to run LMT on a k3s VM from the images CI publishes to GHCR:

- `ghcr.io/rayanmelzi/lmt-backend:latest`
- `ghcr.io/rayanmelzi/lmt-frontend:latest`

Files are numbered in apply order.

## 1. Let the cluster pull from GHCR

GHCR packages are **private by default**. Create a pull secret in the `lmt`
namespace using a GitHub Personal Access Token (classic) with the `read:packages`
scope:

```bash
kubectl create namespace lmt   # or: kubectl apply -f 00-namespace.yaml

kubectl create secret docker-registry ghcr-cred \
  --namespace lmt \
  --docker-server=ghcr.io \
  --docker-username=rayanMELZI \
  --docker-password=<YOUR_GHCR_PAT> \
  --docker-email=you@example.com
```

Alternatively, make each package public on GitHub (Package settings → Danger Zone →
Change visibility) and delete the `imagePullSecrets:` block from
`20-backend.yaml` and `30-frontend.yaml`.

## 2. Set real secrets and hostnames

- Edit `10-mysql-secret.yaml` — replace the placeholder passwords.
- Edit `40-ingress.yaml` — set your real hostnames.
- The frontend talks to the backend at the URL baked in at **build time**
  (`VITE_BACKEND_DOMAIN` in `lmt-frontend/.env`). Set it to your API host
  (e.g. `https://api.lmt.example.com`) and let CI rebuild the frontend image
  before deploying, so the frontend and the ingress `api` host agree.

## 3. Apply

```bash
kubectl apply -f k8s/
```

Watch it come up:

```bash
kubectl -n lmt get pods -w
kubectl -n lmt rollout status deploy/backend
```

## Updating to a new image

CI tags images `latest` and `sha-<commit>`. `latest` doesn't change the pod spec,
so to roll a new build either pin the `sha-...` tag in the Deployment, or force a
re-pull:

```bash
kubectl -n lmt rollout restart deploy/backend deploy/frontend
```

(For repeatable deploys, prefer pinning the `sha-<commit>` tag over `latest`.)
