FROM alpine

ENV VERSION 1.2.0

RUN apk update && \
    apk add --no-cache openjdk8-jre-base && \
    mkdir -p /app/data && cd /app && \
    wget -O mirai-console-wrapper.jar \
        https://github.com/mamoe/mirai-console-wrapper/releases/download/${VERSION}/mirai-console-wrapper-${VERSION}-all.jar

WORKDIR /app/data

ENTRYPOINT ["java", "-jar", "/app/mirai-console-wrapper.jar"]