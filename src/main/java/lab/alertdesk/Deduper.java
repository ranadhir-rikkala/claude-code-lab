package lab.alertdesk;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class Deduper {

    public List<AlertGroup> group(List<Alert> alerts) {
        Map<String, AlertGroup> groups = new LinkedHashMap<>();
        for (Alert alert : alerts) {
            String fingerprint = alert.service() + "::" + alert.message().toLowerCase(Locale.ROOT);
            AlertGroup existing = groups.get(fingerprint);
            groups.put(fingerprint, existing == null ? AlertGroup.of(alert) : existing.add(alert));
        }
        return new ArrayList<>(groups.values());
    }
}
