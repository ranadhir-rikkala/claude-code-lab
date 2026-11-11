package lab.alertdesk;

import java.util.ArrayList;
import java.util.List;

public final class Triage {

    public List<AlertGroup> rank(List<AlertGroup> groups) {
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
}
