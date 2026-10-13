# AGENTS.md — Travel Alert Platform

## Project
Java 17 + Spring Boot 3.3.5 + Maven + H2 (in-memory) + Spring Data JPA.
Package base: `com.example.travelalert`
Build with the Maven wrapper (`mvnw.cmd` on Windows).

## Commands
- Build: `mvnw.cmd clean compile`
- Test: `mvnw.cmd clean test`
- Run: `mvnw.cmd spring-boot:run` (port 8080)
- H2 console: http://localhost:8080/h2-console

Always run `mvnw.cmd clean test` after any change. Do not report success
unless the test suite passes.

## Package Structure
Feature-based verticals under `com.example.travelalert`:
- `common/` — unified notification core (Notification, NotificationService, GlobalExceptionHandler)
- `flight/` — flight price alert vertical
- `train/` — train PNR alert vertical (to be added)
- `hotel/` — hotel saved-search vertical (to be added)

Each vertical follows the same layout:
`model/ → repository/ → dto/ → service/ → controller/`

## Coding Conventions
- Constructor injection only. No field injection, no `@Autowired` on fields.
- DTOs are Java records with Bean Validation annotations.
- Entities are plain classes with explicit getters/setters (no Lombok in new code).
- Money: `BigDecimal`. Dates: `LocalDate`. Timestamps: `Instant`. IDs: `UUID` strings.
- Validation: `@NotBlank`, `@Pattern`, `@Positive`, `@FutureOrPresent`, `@NotNull`.
- Exceptions: custom unchecked exceptions extending `RuntimeException`,
  handled by the existing `GlobalExceptionHandler` (`@RestControllerAdvice`).
- 4-space indentation. No wildcard imports.

## Unified Notification Contract
Every vertical MUST create user-facing notifications through:
NotificationService.create(userId, NotificationType.<FLIGHT|TRAIN|HOTEL>, referenceId, message)
Never create a per-vertical notification repository. Never write to the
`notifications` table directly.

## Concurrency Rule (critical)
Alert triggering must be atomic. Use a conditional UPDATE:
UPDATE <AlertEntity> SET status = :newStatus, triggeredAt = :ts
WHERE id = :id AND status = :currentStatus

Only the caller that observes `rows == 1` creates the notification. This is
the compare-and-swap that prevents duplicate notifications under concurrent
price/PNR checks. Do not replace this with `synchronized` or a global lock.

## Testing
- Tests live under `src/test/java` in the same package as the code under test.
- Class names end in `Test`. Method names describe behavior
  (`priceBelowMaxTriggersNotification`, not `test1`).
- Every vertical includes one **concurrency test**: 20 threads check the same
  alert simultaneously and assert exactly one notification is created.
- Use `@SpringBootTest` for integration tests (they run against H2).

## Commits
Conventional Commits with imperative subject:
`feat(flight): ...`, `fix(train): ...`, `test(hotel): ...`, `chore: ...`, `docs: ...`.
One logical change per commit.

## Hard Rules
1. Do NOT modify files outside the vertical you are working on.
2. Do NOT modify `common/` (NotificationService, Notification, GlobalExceptionHandler)
   without explicit approval.
3. Do NOT add new dependencies to `pom.xml` without explicit approval.
4. Do NOT change the H2 datasource configuration in `application.properties`.
5. Do NOT commit `target/`, `.idea/`, or any IDE files.