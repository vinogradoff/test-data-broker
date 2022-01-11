# test-data-broker

## Start Broker with gradle

Run on default port 8080

`./gradlew bootRun`

Run on any port

`./gradlew bootRun --args='--server.port=<port>'`

## Swagger Documentation of the API

If installed locally:

http://localhost:8080/swagger-ui.html

## Build fat executable jar

`./gradlew bootJar`

## Run fat executable jar

Run on default port 8080

`java - jar test-data-broker-x.y.z.jar`

Run on any port

`java - jar test-data-broker-x.y.z.jar --server.port=<port>`

## Run with docker

amd64 and arm64 are supported.

Run on default port 8080

`docker run -dp 8080:8080 vinogradoff/test-data-broker:latest`

Run on any port

`docker run -dp <port>:8080 vinogradoff/test-data-broker:latest`