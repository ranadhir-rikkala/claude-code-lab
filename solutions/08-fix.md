# Step 08 reference: test and fix

## Failing test (add to DeduperTest)

```java
@Test
void groupsMessagesThatDifferOnlyInCase() {
    List<AlertGroup> groups = deduper.group(List.of(
            new Alert("payments", Severity.CRITICAL, "DB timeout"),
            new Alert("payments", Severity.WARN, "db timeout"),
            new Alert("payments", Severity.WARN, "DB Timeout")));

    assertEquals(1, groups.size());
    assertEquals(3, groups.get(0).count());
    assertEquals(Severity.CRITICAL, groups.get(0).highest());
}
```

## Smallest fix (Deduper)

```java
String fingerprint = alert.service() + "::" + alert.message().toLowerCase(Locale.ROOT);
```

Add `import java.util.Locale;`. Use `Locale.ROOT` so the result doesn't depend on the machine's language settings.

## Expected output after the fix

`12 alerts -> 6 groups`, with `payments CRITICAL 4 DB timeout` at the top.
