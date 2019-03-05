# Stock service assessment

For a recent assignment I was asked to complete an assessment to develop a Java web application to manage and reserve stock. The application was to have a CRUD REST API to save, query, change and remove stock for articles. It should also be possible to reserve stock for 30 minutes. This repository contains the unfiltered results of an early morning developing an application according to the above specification.

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
- Regarding the `ReserveController` and `StockController` classes: Spring Data REST can expose much the same methods automatically already based on the methods available in the Spring Data repositories. However, that requires careful tuning of the methods exposed to prevent unwanted POST/PUT/PATCH/DELETE requests. While I have done so before, I thought it out of scope for this quick example.

- Regarding test coverage I've mostly tried to demonstrate various test strategies rather than achieve 100% coverage in any particular way. Each have their merits, and you'll usually want a mix of these various approaches.

- You'll find I've focused on the backend and exposing a (not yet) REST API; the thymeleaf pages at present only expose a quick view on the data stored. My assumption was that any frontend development will mostly be done in a frontend framework such as Angular.

