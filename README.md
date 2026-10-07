# bikram-sambat

A Java library for working with Bikram Sambat (Nepali) dates, designed to feel
like `java.time.LocalDate`, with accurate conversion to and from the
Gregorian (AD) calendar.

```java
BsDate today = BsDate.now();
BsDate dob = BsDate.of(2055, 4, 12);

LocalDate english = today.toLocalDate();
BsDate fromEnglish = BsDate.from(LocalDate.of(2026, 9, 29));

if (dob.isBefore(today)) {
    long years = dob.until(today, ChronoUnit.YEARS);
}
```

## Project structure

This is a multi-module Maven project (Java 21) with one published distribution:

- `core` (`bikram-sambat-core`) contains the `BsDate` type, conversion engine,
  with no JSON library dependency.
- `jackson` (`bikram-sambat-jackson`) provides JSON serialization and
  deserialization for `BsDate`.
- `jpa` (`bikram-sambat-jpa`) contains the JPA `AttributeConverter`.
- `spring` (`bikram-sambat-spring`) adds Spring MVC request parameter
  conversion, with Spring Boot auto-configuration.
- `distribution` (`bikram-sambat`) combines these modules into one published
  jar. Consumers add only this artifact; internal modules are not deployed.

Add the single distribution dependency:

```xml
<dependency>
    <groupId>io.github.sujitsharma</groupId>
    <artifactId>bikram-sambat</artifactId>
    <version>v2.0.0</version>
</dependency>
```

The distribution contains the Jackson module. Register it with your mapper, or
let Jackson discover it through the module's service registration:

```java
ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
// Alternatively: mapper.registerModule(new BsDateJacksonModule());
```

`BsDate` fields in request and response POJOs are then read and written as
`"yyyy-MM-dd"` JSON strings.

### Spring MVC request parameters

The distribution includes Spring conversion support. In a Spring Boot MVC
application, the module registers itself automatically, so a request such as
`?date=2083-06-20` binds to a `BsDate` controller argument:

```java
@GetMapping("/users")
public UserResponse getUser(@RequestParam BsDate date) {
    // date is BsDate.parse("2083-06-20")
}
```

The MVC conversion service registers all four conversions:
`String` ↔ `BsDate` and `LocalDate` ↔ `BsDate`. This supports `@RequestParam`,
`@PathVariable`, and `@ModelAttribute` binding, and formats `BsDate` values as
`yyyy-MM-dd` when converting them to strings.

For Spring MVC without Spring Boot, import
`io.github.sujitsharma.bikramsambat.spring.BsDateMvcConfiguration` into the
MVC application context. Invalid date strings are reported as Spring request
conversion errors.

## JPA / Hibernate

The distribution includes JPA support alongside the date and Jackson APIs. Its converter stores the
Gregorian date corresponding to a `BsDate`; Hibernate maps `LocalDate` to SQL
`DATE`. The converter has `autoApply = true`, so it applies to mapped
`BsDate` attributes once the JPA provider discovers the converter class.

```xml
<dependency>
    <groupId>io.github.sujitsharma</groupId>
    <artifactId>bikram-sambat</artifactId>
    <version>v2.0.0</version>
</dependency>
```

```java
@Entity
class Customer {
    @Id
    private Long id;

    private BsDate birthDate;
}
```

If your persistence setup does not discover converters automatically, list
`io.github.sujitsharma.bikramsambat.jpa.BsDateAttributeConverter` in the
persistence unit or annotate the field with `@Convert(converter =
BsDateAttributeConverter.class)`.

### Spring Data JPA date queries

The converter also applies to query parameters, so Spring Data derived range
and comparison queries work with `BsDate` fields. Use `BsDate` (the type name in
this library) for the repository parameters:

```java
interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByDateOfBirthBetween(BsDate from, BsDate to);

    List<User> findByDateOfBirthGreaterThanEqual(BsDate date);
}
```

Spring Data builds the database predicates, and the JPA converter converts
the supplied BS dates to Gregorian SQL `DATE` values for comparison. No custom
query implementation or Spring Data dependency in this library is needed.

The same conversion applies to typed JPA Criteria queries:

```java
CriteriaBuilder cb = entityManager.getCriteriaBuilder();
CriteriaQuery<User> query = cb.createQuery(User.class);
Root<User> user = query.from(User.class);
Path<BsDate> dateOfBirth = user.get("dateOfBirth");

query.where(cb.between(dateOfBirth, from, to));
// Or: query.where(cb.greaterThanOrEqualTo(dateOfBirth, date));
```

`BsDate` attributes may be nullable. The converter passes database `NULL`
through as Java `null` and vice versa. For nullable dates, use
`cb.isNull(dateOfBirth)` or `cb.isNotNull(dateOfBirth)`; SQL comparisons such as
`>= null` or `between null and ...` do not match null rows.

## Building

```bash
mvn test
```

## Known data caveat

The BS↔AD conversion table currently covers years 1970–2100 BS. Three of
those years (1974, 1990, 2096) sum to 364 days in the source data instead of
the required 365/366, and are flagged with a `NOTE` comment in
`BsCalendarData`. This is being cross-checked against authoritative sources
before the first release; dates in or after an affected year may currently be
off by a day until corrected.

## License

MIT
