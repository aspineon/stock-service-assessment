# Stock service


## Notes
- Regarding the `ReserveController` and `StockController` classes: Spring Data REST can expose much the same methods
automatically already based on the methods available in the Spring Data repositories. However, that requires careful
tuning of the methods exposed to prevent unwanted POST/PUT/PATCH/DELETE requests. While I have do so before, I thought
it out of scope for this quick example.

- Regarding test coverage I've mostly tried to demonstrate various test strategies rather than achieve 100% coverage in
one particular way. Each have their merits, and you'll usually want a mix of these various approaches.
