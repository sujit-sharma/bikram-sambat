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

This is a multi-module Maven project (Java 21):

- `core` (artifact `bikram-sambat`) — the `BsDate` type, BS↔AD conversion
  engine, plus Jackson serialization and deserialization support. This is the
  only module required for calendar and JSON operations.
- `jpa` (artifact `bikram-sambat-jpa`) — JPA `AttributeConverter` support for
  storing `BsDate` values in SQL `DATE` columns.

Planned modules, added incrementally:

- `bikram-sambat-jackson` — Jackson serialization/deserialization support.
- `bikram-sambat-spring-boot-starter` — auto-configuration wiring the above.

## JPA / Hibernate

Add `bikram-sambat-jpa` alongside your JPA provider. Its converter stores the
Gregorian date corresponding to a `BsDate`; Hibernate maps `LocalDate` to SQL
`DATE`. The converter has `autoApply = true`, so it applies to mapped
`BsDate` attributes once the JPA provider discovers the converter class.

```xml
<dependency>
    <groupId>io.github.sujitsharma</groupId>
    <artifactId>bikram-sambat-jpa</artifactId>
    <version>0.1.0</version>
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
