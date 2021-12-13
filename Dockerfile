FROM openjdk:11-jre-slim
MAINTAINER Alexei Vinogradov
WORKDIR /opt/test-data-broker
COPY build/libs/test-data-broker-0.1.2.jar .
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "test-data-broker-0.1.2.jar"]

