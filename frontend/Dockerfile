### STAGE 1: Build ###
FROM node:18 AS build

WORKDIR /usr/src/app
COPY . .
RUN npm ci
RUN npm run build-docker


### STAGE 2: Run ###
FROM nginx:1.17.6-alpine

COPY nginx.conf /etc/nginx/nginx.conf

RUN rm -R /usr/share/nginx/html/*
WORKDIR /usr/share/nginx/html
COPY --from=build /usr/src/app/dist/burning-okr-docker/ .
