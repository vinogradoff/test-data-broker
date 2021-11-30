# test-data-broker

## Start Broker with gradle

Run on default port 8080

`./gradlew bootRun`

Run on any port

`./gradlew bootRun --args='--server.port=<port>`

## Build fat executable jar

`./gradlew bootJar`

## Run fat executable jar

Run on default port 8080

`java - jar test-data-broker-x.y.z.jar`

Run on any port

`java - jar test-data-broker-x.y.z.jar --server.port=<port>`
