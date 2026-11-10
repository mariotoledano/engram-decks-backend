# Engram Decks API

RESTful API for flashcard decks. Built with Java 21, Spring Boot 4.1, and MongoDB.

## Requirements

- JDK 21
- MongoDB listening on port 27017

## Run

The connection string lives in `src/main/resources/application.properties`:

```properties
spring.mongodb.uri=mongodb://localhost:27017/engram-decks
```

Windows:

```bat
mvnw.cmd spring-boot:run
```

Unix:

```sh
./mvnw spring-boot:run
```

The API listens on port 8080.

## Check

- Health: [http://localhost:8080/actuator/health](http://localhost:8080/actuator/health)
- Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- Base path: `/api/v1`

Swagger UI is the full request and response contract. The routes below are the map.

| Method | Path |
| --- | --- |
| `GET`, `POST` | `/api/v1/decks` |
| `GET`, `PUT`, `DELETE` | `/api/v1/decks/{id}` |
| `GET`, `POST` | `/api/v1/decks/{deckId}/flashcards` |
| `GET`, `PUT`, `PATCH`, `DELETE` | `/api/v1/flashcards/{id}` |

Deleting a deck also deletes its flashcards.

## Tests

Windows:

```bat
mvnw.cmd test
```

Unix:

```sh
./mvnw test
```

`FlashcardServiceImplTest` uses mocks and does not need MongoDB. `EngramDecksBackendApplicationTests` boots the full application context, so MongoDB must be running.

## Layout

Packages under `com.engramdecks.api`:

| Package | Role |
| --- | --- |
| `config` | Mongo auditing, OpenAPI, and CORS |
| `controller` | REST endpoints |
| `dto` | Request and response records |
| `exception` | Domain exceptions and the global handler |
| `mapper` | Entity and DTO conversion |
| `model` | MongoDB documents |
| `repository` | Spring Data repositories |
| `service` | Business interfaces and implementations |

Controllers accept request records and return response records. Services map documents to those records.

## References

- [Spring Boot MongoDB](https://docs.spring.io/spring-boot/4.1.2/reference/data/nosql.html#data.nosql.mongodb)
- [Spring Boot Actuator](https://docs.spring.io/spring-boot/4.1.2/reference/actuator/index.html)
- [springdoc-openapi](https://springdoc.org/)
