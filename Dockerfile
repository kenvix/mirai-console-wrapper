FROM alpine

ENV VERSION 1.2.0

RUN apk update && \
    apk add --no-cache tzdata ca-certificates openjdk8-jre-base && \
    ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
    update-ca-certificates ; \
    mkdir -p /app/data && cd /app && \
    wget -O mirai-console-wrapper.jar \
        https://github.com/mamoe/mirai-console-wrapper/releases/download/${VERSION}/mirai-console-wrapper-${VERSION}-all.jar

WORKDIR /app/data

ENTRYPOINT ["java", "-jar", "/app/mirai-console-wrapper.jar"]
