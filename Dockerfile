FROM maven:3.6.1-jdk-12 AS builder
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src/ /build/src/
RUN mvn package

FROM openjdk:12 as Target

ENV MONGO_URL=pad-b-config-database \
EUREKA_URL=pad-b-registry \ 
SERVER_URL=pad-b-config \
MONGO_DB=admin \
MONGO_USER=admin \
MONGO_PASSWORD=123456 \
DOCKERIZE_VERSION=v0.6.0 \
MONGO_PORT=27018 


COPY --from=builder /build/target/config-1.0.0.jar config.jar

CMD wget https://github.com/jwilder/dockerize/releases/download/$DOCKERIZE_VERSION/dockerize-alpine-linux-amd64-$DOCKERIZE_VERSION.tar.gz \
&& tar -C /usr/local/bin -xzvf dockerize-alpine-linux-amd64-$DOCKERIZE_VERSION.tar.gz \
&& rm dockerize-alpine-linux-amd64-$DOCKERIZE_VERSION.tar.gz && dockerize -wait tcp://$EUREKA_URL:8761 -timeout 60m yarn start && dockerize -wait tcp://$MONGO_URL:$MONGO_PORT -timeout 60m yarn start

ENTRYPOINT ["java","-jar","config.jar"]

EXPOSE 8086