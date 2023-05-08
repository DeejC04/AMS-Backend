# AMS: Spring Boot Backend

Quick start for Spring Boot and Gradle.

## How to build

Create `application.properties` file in `src/main/resources/` and set contents to:

```properties
spring.application.name=ams-backend
spring.jackson.property-naming-strategy=SNAKE_CASE
```

Build with Gradle wrapper:

```sh
$ ./gradlew clean build
```

## How to run

Run with Gradle wrapper:

```sh
$ ./gradlew bootRun
```

## Testing with Curl

```sh
$ curl http://localhost:8080/hi?name=Gabe
"Hi Gabe"
```
