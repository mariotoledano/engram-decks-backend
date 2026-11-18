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

Windows (helper):

```bat
dev-utils\init-api.bat
```

Or with Maven:

```bat
mvnw.cmd spring-boot:run
```

Unix:

```sh
./mvnw spring-boot:run
```

The API listens on port 8080.

## Dev utilities

Scripts under [`dev-utils/`](dev-utils/):

| File | Purpose |
| --- | --- |
| [`dev-utils/init-api.bat`](dev-utils/init-api.bat) | Starts the API locally. Prints the Maven environment, then health and Swagger links once the server is ready |
| [`dev-utils/mongo/seed-dev.mongodb.js`](dev-utils/mongo/seed-dev.mongodb.js) | Wipes and seeds sample decks/flashcards in MongoDB |
| [`dev-utils/api-docs-json/download-api-docs.bat`](dev-utils/api-docs-json/download-api-docs.bat) | Downloads OpenAPI JSON from a running API into `dev-utils/api-docs-json/openapi.json` (local file; not committed) |

Seed example (MongoDB must be running):

```bat
mongosh "mongodb://localhost:27017/engram-decks" --file dev-utils\mongo\seed-dev.mongodb.js
```

## Check

- Health: [http://localhost:8080/actuator/health](http://localhost:8080/actuator/health)
- Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- Base path: `/api/v1`
- OpenAPI JSON (app running): [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

Swagger UI is the full request and response contract. The routes below are the map.

| Method | Path |
| --- | --- |
| `GET`, `POST` | `/api/v1/decks` |
| `GET`, `PUT`, `PATCH`, `DELETE` | `/api/v1/decks/{id}` |
| `GET`, `POST` | `/api/v1/decks/{deckId}/flashcards` |
| `DELETE` | `/api/v1/decks/{deckId}/flashcards/{flashcardId}` |
| `GET` | `/api/v1/flashcards/unlinked` |
| `GET`, `POST` | `/api/v1/flashcards` |
| `GET`, `PUT`, `PATCH`, `DELETE` | `/api/v1/flashcards/{id}` |
| `GET` | `/api/v1/tags` |
| `POST` | `/api/v1/study-sessions/preview` |
| `POST` | `/api/v1/study-sessions` |
| `GET` | `/api/v1/maintenance/summary` |
| `GET` | `/api/v1/maintenance/flashcards` |
| `DELETE` | `/api/v1/maintenance/decks` |
| `DELETE` | `/api/v1/maintenance/flashcards` |
| `DELETE` | `/api/v1/maintenance/unlinked` |

Deleting a deck unlinks its flashcards. A flashcard that still belongs to another deck stays there. Maintenance routes are registered only when `engram.maintenance.enabled=true` (the local default). Each wipe requires a JSON body `{ "confirm": "BORRAR" }`. CORS allows localhost and private LAN addresses so a phone running Expo can reach the API; set the flag to `false` before exposing the port beyond that network, because the confirmation word is not a credential. A study-session preview does not create a deck. Saving a session creates a deck and links the existing flashcards. Creating a flashcard with `POST /api/v1/decks/{deckId}/flashcards` links it only to the deck id in the path and ignores `deckIds` in the body.

## Tests

Windows:

```bat
mvnw.cmd test
```

Unix:

```sh
./mvnw test
```

`DeckServiceImplTest`, `FlashcardServiceImplTest`, `StudySessionServiceImplTest`, `MaintenanceServiceImplTest`, `DeckMapperTest`, `TagNormalizerTest`, `DeckCoverRequestValidatorTest`, and `DeckControllerTest` use mocks or MockMvc and do not need MongoDB. `EngramDecksBackendApplicationTests` boots the full application context, so MongoDB must be running.

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
