package lab.alertdesk;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

class TriageTest {

    @Test
    void ranksBySeverityThenCount() {
        AlertGroup criticalOnce = new AlertGroup(new Alert("inventory", Severity.CRITICAL, "Disk full"), 1, Severity.CRITICAL);
        AlertGroup warnThrice = new AlertGroup(new Alert("search", Severity.WARN, "Latency"), 3, Severity.WARN);
        AlertGroup warnOnce = new AlertGroup(new Alert("checkout", Severity.WARN, "Queue depth"), 1, Severity.WARN);

        List<AlertGroup> ranked = new Triage().rank(List.of(warnOnce, criticalOnce, warnThrice));

        assertEquals(List.of(criticalOnce, warnThrice, warnOnce), ranked);
    }
}
