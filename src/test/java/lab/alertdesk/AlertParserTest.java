package lab.alertdesk;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.Test;

class AlertParserTest {

    private final AlertParser parser = new AlertParser();

    @Test
    void parsesServiceSeverityAndMessage() {
        Alert alert = parser.parse("payments|CRITICAL|DB timeout");

        assertEquals(new Alert("payments", Severity.CRITICAL, "DB timeout"), alert);
    }

    @Test
    void acceptsLowercaseSeverity() {
        Alert alert = parser.parse("search|warn|High latency");

        assertEquals(Severity.WARN, alert.severity());
    }

    @Test
    void keepsPipesInsideTheMessage() {
        Alert alert = parser.parse("api|INFO|status=200|cached");

        assertEquals("status=200|cached", alert.message());
    }

    @Test
    void rejectsLineWithoutThreeParts() {
        assertThrows(IllegalArgumentException.class, () -> parser.parse("payments|CRITICAL"));
    }

    @Test
    void skipsCommentsAndBlankLines() {
        List<Alert> alerts = parser.parseAll(List.of("# header", "", "auth|INFO|Token refresh"));

        assertEquals(1, alerts.size());
    }
}
