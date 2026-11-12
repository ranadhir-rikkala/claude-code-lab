package lab.alertdesk;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

class DeduperTest {

    private final Deduper deduper = new Deduper();

    @Test
    void groupsIdenticalAlerts() {
        Alert alert = new Alert("search", Severity.WARN, "High latency");

        List<AlertGroup> groups = deduper.group(List.of(alert, alert, alert));

        assertEquals(1, groups.size());
        assertEquals(3, groups.get(0).count());
    }

    @Test
    void keepsHighestSeverityInGroup() {
        List<AlertGroup> groups = deduper.group(List.of(
                new Alert("payments", Severity.WARN, "DB timeout"),
                new Alert("payments", Severity.CRITICAL, "DB timeout")));

        assertEquals(Severity.CRITICAL, groups.get(0).highest());
    }

    @Test
    void keepsDifferentServicesApart() {
        List<AlertGroup> groups = deduper.group(List.of(
                new Alert("payments", Severity.WARN, "DB timeout"),
                new Alert("orders", Severity.WARN, "DB timeout")));

        assertEquals(2, groups.size());
    }

    @Test
    void groupsMessagesDifferingOnlyInCase() {
        List<AlertGroup> groups = deduper.group(List.of(
                new Alert("payments", Severity.WARN, "DB timeout"),
                new Alert("payments", Severity.WARN, "db timeout"),
                new Alert("payments", Severity.WARN, "DB Timeout")));

        assertEquals(1, groups.size());
        assertEquals(3, groups.get(0).count());
    }
}
