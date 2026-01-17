# Étape build
FROM node:22-alpine AS builder

WORKDIR /app

COPY package.json yarn.lock* ./
RUN yarn install

COPY . .
RUN yarn build

# Étape runtime
FROM node:22-alpine

WORKDIR /app

COPY --from=builder /app/.output ./.output
COPY --from=builder /app/package.json ./

EXPOSE 3000
CMD ["node", ".output/server/index.mjs"]
