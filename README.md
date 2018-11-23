# Stock service

## Features
- Spring Boot 2.1
- Spring Data JPA
- Flyway for database versioning + H2 test database
- Spring Rest Docs
- Thymeleaf for (limited) HTML pages

## Implementation choices
- Package per feature, with visibility reduced as much as possible.
- Separate tables for `stocks` and `reserved_stocks`, with database transactions on service methods.
- Exceptions annotated with `@ResponseStatus` for simplicity in `@RestController`s.
- JPA Auditing used for `reserved_stocks` columns `created_date`, `created_by`, `modified_date`, `modified_by`.
- Scheduled method call to remove any expired `reserved_stocks` based on `created_date` every minute.
- API documentation snippets generated from on web controller test runs.

## Notes
- Regarding the `ReserveController` and `StockController` classes: Spring Data REST can expose much the same methods
automatically already based on the methods available in the Spring Data repositories. However, that requires careful
tuning of the methods exposed to prevent unwanted POST/PUT/PATCH/DELETE requests. While I have do so before, I thought
it out of scope for this quick example.

- You'll find I've focused on the backend and exposing a (not yet) REST API; the thymeleaf pages at present only expose
a quick view on the data stored. My assumption was that any frontend development will mostly be done in a frontend
framework such as Angular.

- Regarding test coverage I've mostly tried to demonstrate various test strategies rather than achieve 100% coverage in
any particular way. Each have their merits, and you'll usually want a mix of these various approaches.

- Regarding Maven pom.xml: While familiar with Gradle, I prefer Maven for consistency across a landscape of services. ;)
