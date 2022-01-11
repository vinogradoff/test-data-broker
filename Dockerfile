FROM openjdk:11-jre-slim
MAINTAINER Alexei Vinogradov
ARG BROKER_VERSION
WORKDIR /opt/test-data-broker
COPY build/libs/test-data-broker-$BROKER_VERSION.jar .
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "test-data-broker-$BROKER_VERSION.jar"]

