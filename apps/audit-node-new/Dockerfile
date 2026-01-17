FROM node:22-alpine

# Dépendances nécessaires à Chromium
RUN apk add --no-cache \
  chromium \
  nss \
  freetype \
  harfbuzz \
  ca-certificates \
  ttf-freefont

ENV PUPPETEER_SKIP_CHROMIUM_DOWNLOAD=true \
    PUPPETEER_EXECUTABLE_PATH=/usr/bin/chromium-browser

WORKDIR /app

COPY package.json yarn.lock* ./
RUN yarn install

COPY . .

CMD ["node", "lighthouseAudit.js"]
