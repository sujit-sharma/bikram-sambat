# bikram-sambat (Modern Nepali Date Library for Java & Spring Boot)

[![Java 21](https://img.shields.io/badge/Java-21-orange?logo=openjdk)](https://openjdk.org/projects/jdk/21/)
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

A production-ready, highly accurate Java library for working with **Bikram Sambat (Nepali) dates**. Unlike older, unmaintained libraries, this project is built from scratch using modern **Java 21** standards, designed to match the native `java.time.LocalDate` ecosystem seamlessly.

### 🌟 Why choose this library over older alternatives?
* **Zero Legacy Risk:** Actively maintained, type-safe, and fully tested.
* **First-Class Spring & Enterprise Integration:** Native support for automated Jackson JSON serialization and JPA/Hibernate database persistence out-of-the-box.
* **More than just a Converter:** Full support for date manipulation, comparison, formatting, and time-unit differences (`ChronoUnit`).


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
    <version>2.0.0</version>
</dependency>
```

The distribution contains serializers for Jackson 2 and Jackson 3. Spring
Boot auto-configuration selects Jackson 3 when it is present, otherwise it
provides the Jackson 2 module bean. Only the matching module bean is created.
For manually created mappers, register the version-specific module or let
Jackson discover it through service registration:

```java
// Jackson 2
ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
// Or: mapper.registerModule(new BsDateJacksonModule());
```

The Jackson 3 manual equivalent is `new BsDateJackson3Module()` with Jackson
3's `JsonMapper`. The Jackson 2 and 3 APIs use different packages; depend on
the version your application uses. This project is compiled for Java 17, so
its artifacts run on Java 17 or newer.

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

The Spring module registers MVC date conversion with Spring Boot 2.x through
`spring.factories` and with Spring Boot 3.x through
`AutoConfiguration.imports`. It includes Bean Validation constraints for both
API namespaces. For Boot 3, import constraints from
`io.github.sujitsharma.bikramsambat.spring.validation`; for Boot 2, import them
from `io.github.sujitsharma.bikramsambat.spring.validation.v2`. In either
version, combine `@NotNull` with `@BsPast`, `@BsFuture`, `@BsPastOrPresent`, or
`@BsFutureOrPresent` as needed:

```java
import io.github.sujitsharma.bikramsambat.BsDate;
import io.github.sujitsharma.bikramsambat.spring.validation.BsPast;
import jakarta.validation.constraints.NotNull;

public class PersonRequest {
    @NotNull
    @BsPast
    private BsDate dateOfBirth;
}
```

These constraints compare against today's BS date using the validation clock.
They treat `null` as valid, following Bean Validation conventions, so use
`@NotNull` when a value is required.

The JPA module includes converters for both persistence APIs: the default
`io.github.sujitsharma.bikramsambat.jpa.BsDateAttributeConverter` uses
`jakarta.persistence` for Boot 3, and
`io.github.sujitsharma.bikramsambat.jpa.javax.BsDateAttributeConverter` uses
`javax.persistence` for Boot 2. Both persist the Gregorian equivalent as SQL
`DATE`. Both converters use `@Converter(autoApply = true)`, so you do not need
to annotate every `BsDate` field with `@Convert` once the converter is included
in the persistence unit.

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
    <version>2.0.0</version>
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

If Spring Boot does not discover the converter in the dependency jar, add its
package to entity scanning once. Include your entity package as well, since
`@EntityScan` defines the packages Spring Boot scans for managed JPA types:

```java
@SpringBootApplication
@EntityScan(basePackages = {
    "com.example.app.entity",
    "io.github.sujitsharma.bikramsambat.jpa"
})
public class Application {
}
```

The library package scan finds the converter that matches the active JPA API:
Boot 2 discovers the `javax.persistence` converter; Boot 3 discovers the
`jakarta.persistence` converter. JPA's `autoApply` rule then applies it to
every supported `BsDate` attribute in that persistence unit. Alternatively,
declare the matching converter once in the persistence unit's `orm.xml`.

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

### Keywords & Use Cases
If you are searching for any of the following solutions in Java, this library is built for you:
* **Nepali Miti Converter Java:** Easily convert historical and current Nepalese calendar dates.
* **BS to AD & AD to BS Converter:** High-precision bi-directional calendar mapping.
* **Spring Boot Nepali Date Support:** Bind `BsDate` directly to incoming JSON payloads and REST API endpoints.
* **JPA Hibernate Nepali Date:** Persist Bikram Sambat dates seamlessly into database columns as standard SQL `DATE` types.

## License

MIT
