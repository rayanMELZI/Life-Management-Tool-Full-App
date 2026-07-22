# Vite + React static site: build with pnpm, serve the static bundle with nginx.

# ---- build ----
FROM node:22-bookworm-slim AS build
WORKDIR /app
RUN corepack enable

# Install deps first (better layer caching). Needs both files for --frozen-lockfile.
COPY package.json pnpm-lock.yaml ./
RUN pnpm install --frozen-lockfile

# Build the static bundle (vite outputs to /app/build per vite.config.ts).
# NOTE: VITE_BACKEND_DOMAIN is baked in at build time from lmt-frontend/.env.
# Point it at your production API before this image is built. You can also
# override it here with: --build-arg VITE_BACKEND_DOMAIN=https://api.example.com
COPY . .
ARG VITE_BACKEND_DOMAIN
RUN pnpm build

# ---- runtime ----
FROM nginx:1.27-alpine AS runner
COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /app/build /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
