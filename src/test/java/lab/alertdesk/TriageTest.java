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

        List<AlertGroup> ranked = new Triage().prioritize(List.of(warnOnce, criticalOnce, warnThrice));

        assertEquals(List.of(criticalOnce, warnThrice, warnOnce), ranked);
    }

    @Test
    void atLeastWarnKeepsWarnAndCriticalAndDropsInfo() {
        AlertGroup info = new AlertGroup(new Alert("web", Severity.INFO, "Deploy done"), 2, Severity.INFO);
        AlertGroup warn = new AlertGroup(new Alert("search", Severity.WARN, "Latency"), 1, Severity.WARN);
        AlertGroup critical = new AlertGroup(new Alert("inventory", Severity.CRITICAL, "Disk full"), 1, Severity.CRITICAL);

        List<AlertGroup> kept = new Triage().atLeast(List.of(info, warn, critical), Severity.WARN);

        assertEquals(List.of(warn, critical), kept);
    }

    @Test
    void atLeastInfoKeepsEverythingInInputOrder() {
        AlertGroup critical = new AlertGroup(new Alert("inventory", Severity.CRITICAL, "Disk full"), 1, Severity.CRITICAL);
        AlertGroup info = new AlertGroup(new Alert("web", Severity.INFO, "Deploy done"), 2, Severity.INFO);
        AlertGroup warn = new AlertGroup(new Alert("search", Severity.WARN, "Latency"), 1, Severity.WARN);
        List<AlertGroup> groups = List.of(critical, info, warn);

        List<AlertGroup> kept = new Triage().atLeast(groups, Severity.INFO);

        assertEquals(groups, kept);
    }

    @Test
    void atLeastCriticalKeepsOnlyCritical() {
        AlertGroup info = new AlertGroup(new Alert("web", Severity.INFO, "Deploy done"), 2, Severity.INFO);
        AlertGroup warn = new AlertGroup(new Alert("search", Severity.WARN, "Latency"), 1, Severity.WARN);
        AlertGroup critical = new AlertGroup(new Alert("inventory", Severity.CRITICAL, "Disk full"), 1, Severity.CRITICAL);

        List<AlertGroup> kept = new Triage().atLeast(List.of(info, warn, critical), Severity.CRITICAL);

        assertEquals(List.of(critical), kept);
    }

    @Test
    void atLeastUsesHighestSeverityNotFirstAlert() {
        AlertGroup escalated = new AlertGroup(new Alert("db", Severity.INFO, "Slow query"), 3, Severity.CRITICAL);

        List<AlertGroup> kept = new Triage().atLeast(List.of(escalated), Severity.WARN);

        assertEquals(List.of(escalated), kept);
    }
}
