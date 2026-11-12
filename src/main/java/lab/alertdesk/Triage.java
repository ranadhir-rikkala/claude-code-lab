package lab.alertdesk;

import java.util.ArrayList;
import java.util.List;

public final class Triage {

    public List<AlertGroup> prioritize(List<AlertGroup> groups) {
        List<AlertGroup> ranked = new ArrayList<>(groups);
        ranked.sort((a, b) -> {
            int bySeverity = b.highest().compareTo(a.highest());
            if (bySeverity != 0) {
                return bySeverity;
            }
            return Integer.compare(b.count(), a.count());
        });
        return ranked;
    }

    public List<AlertGroup> atLeast(List<AlertGroup> groups, Severity min) {
        List<AlertGroup> kept = new ArrayList<>();
        for (AlertGroup group : groups) {
            if (group.highest().compareTo(min) >= 0) {
                kept.add(group);
            }
        }
        return kept;
    }
}
