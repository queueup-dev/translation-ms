FROM openjdk:8-jre-alpine

RUN apk add --update curl wget && rm -rf /var/cache/apk/*