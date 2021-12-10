FROM adoptopenjdk/openjdk11:jre
MAINTAINER Alexei Vinogradov
WORKDIR /opt/test-data-broker
COPY build/libs/test-data-broker-0.1.1.jar .
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "test-data-broker-0.1.1.jar"]

