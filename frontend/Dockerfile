FROM node:20-alpine AS builder
WORKDIR /app
COPY package.json package-lock.json tsconfig.json tsconfig.node.json vite.config.ts postcss.config.js tailwind.config.js index.html ./
COPY src ./src
RUN npm ci --legacy-peer-deps
RUN npm run build

FROM nginx:stable-alpine
COPY --from=builder /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/nginx.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
