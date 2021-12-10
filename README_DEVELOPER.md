## release on maven central

Security & Signing settings must be in gradle.properties

1. update version on build.gradle
2. `./gradlew publish closeAndReleaseRepository`

## publish docker image for arm64 (mac m1) and amd64 architectures

1. `docker login`
2. `docker buildx build --platform linux/amd64,linux/arm64 -t vinogradoff/test-data-broker:x.y.z -t vinogradoff/test-data-broker:latest . --push`