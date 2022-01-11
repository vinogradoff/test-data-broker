## release on maven central

Security & Signing settings must be in gradle.properties

1. update version on build.gradle
2. `./gradlew publish closeAndReleaseRepository`

## publish docker image for arm64 (mac m1) and amd64 architectures

You might need to create a new driver with eg. `docker buildx create --use` for the first execution.

1. `docker login`
2. `docker buildx build --build-arg BROKER_VERSION=x.y.z --platform linux/amd64,linux/arm64 -t vinogradoff/test-data-broker:x.y.z -t vinogradoff/test-data-broker:latest . --push`