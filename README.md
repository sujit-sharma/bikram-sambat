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
  engine, and no third-party runtime dependencies. This is the only module
  published so far.

Planned modules, added incrementally:

- `bikram-sambat-jackson` — Jackson serialization/deserialization support.
- `bikram-sambat-jpa` — JPA/Hibernate `AttributeConverter` for persisting
  `BsDate` as a Gregorian `DATE` column.
- `bikram-sambat-spring-boot-starter` — auto-configuration wiring the above.

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