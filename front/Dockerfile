
FROM node:18-slim AS build-stage

WORKDIR /app

COPY package*.json ./
COPY . .

RUN rm -rf node_modules package-lock.json && npm cache clean --force && npm install

RUN npm run build

FROM nginx:alpine AS production-stage

COPY default.conf /etc/nginx/conf.d/default.conf

COPY --from=build-stage /app/dist /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]