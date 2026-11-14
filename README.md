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
| `GET`, `PUT`, `PATCH`, `DELETE` | `/api/v1/decks/{id}` |
| `GET`, `POST` | `/api/v1/decks/{deckId}/flashcards` |
| `GET`, `PUT`, `PATCH`, `DELETE` | `/api/v1/flashcards/{id}` |

Deleting a deck also deletes its flashcards.

OpenAPI JSON (app running): [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

## Local dev-utils

The `dev-utils/` folder is listed in `.gitignore`. Use it for personal scripts and MongoDB seeds. It is not committed to git.

Requirements:

- [mongosh](https://www.mongodb.com/docs/mongodb-shell/) for seeds

Scripts (create or refresh them locally):

| File | Purpose |
| --- | --- |
| `dev-utils/mongo/seed-dev.mongodb.js` | Reset and load dev decks (4 mazos + ~128 flashcards) |

Example (from the repo root):

```bat
mongosh "mongodb://localhost:27017/engram-decks" --file dev-utils/mongo/seed-dev.mongodb.js
```

Seed content (after running the script):

| Deck (name) | Content |
| --- | --- |
| Filosofía estoica | ~32 flashcards (estoicismo clásico y práctica) |
| Programación | ~32 flashcards (fundamentos y buenas prácticas) |
| Psicología | ~32 flashcards (cognición, teorías, sesgos) |
| Neurociencia | ~32 flashcards (cerebro, memoria, métodos) |

Deck and flashcard `_id` values are **MongoDB ObjectIds** (24-char hex), the same shape the API generates on create—not fixed strings. After seeding, `mongosh` prints each deck id; use it in `GET /api/v1/decks/<id>/flashcards`.

**Compass:** select database `engram-decks`, open Mongosh, paste the full `seed-dev.mongodb.js` and run (same as `mongosh --file`).

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
