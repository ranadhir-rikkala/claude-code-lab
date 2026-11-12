package lab.alertdesk;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class AppTest {

    @Test
    void defaultsToInfoWhenOptionAbsent() {
        assertEquals(Severity.INFO, App.parseMinSeverity(new String[] {}));
        assertEquals(Severity.INFO, App.parseMinSeverity(new String[] {"data/alerts.txt"}));
    }

    @Test
    void parsesMinSeverityValue() {
        String[] args = {"data/alerts.txt", "--min-severity", "WARN"};

        assertEquals(Severity.WARN, App.parseMinSeverity(args));
    }

    @Test
    void acceptsLowercaseValue() {
        assertEquals(Severity.WARN, App.parseMinSeverity(new String[] {"--min-severity", "warn"}));
    }

    @Test
    void rejectsMissingValue() {
        assertThrows(IllegalArgumentException.class,
                () -> App.parseMinSeverity(new String[] {"--min-severity"}));
    }

    @Test
    void rejectsUnknownValue() {
        assertThrows(IllegalArgumentException.class,
                () -> App.parseMinSeverity(new String[] {"--min-severity", "LOUD"}));
    }

    @Test
    void inputPathDefaultsToSampleData() {
        assertEquals("data/alerts.txt", App.inputPath(new String[] {}));
        assertEquals("data/alerts.txt", App.inputPath(new String[] {"--min-severity", "WARN"}));
    }

    @Test
    void inputPathSkipsOptionValueInAnyPosition() {
        assertEquals("data/x.txt", App.inputPath(new String[] {"data/x.txt", "--min-severity", "WARN"}));
        assertEquals("data/x.txt", App.inputPath(new String[] {"--min-severity", "WARN", "data/x.txt"}));
    }

    @Test
    void inputPathRejectsUnknownOption() {
        assertThrows(IllegalArgumentException.class,
                () -> App.inputPath(new String[] {"--min-severity=WARN"}));
        assertThrows(IllegalArgumentException.class,
                () -> App.inputPath(new String[] {"--min-severty", "WARN", "data/x.txt"}));
    }

    @Test
    void inputPathRejectsSecondFile() {
        assertThrows(IllegalArgumentException.class,
                () -> App.inputPath(new String[] {"data/a.txt", "data/b.txt"}));
    }
}
