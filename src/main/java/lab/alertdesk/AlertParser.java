package lab.alertdesk;

import java.util.ArrayList;
import java.util.List;

public final class AlertParser {

    public Alert parse(String line) {
        String[] parts = line.split("\\|", 3);
        if (parts.length != 3) {
            throw new IllegalArgumentException("Expected service|SEVERITY|message but got: " + line);
        }
        Severity severity = Severity.valueOf(parts[1].trim().toUpperCase());
        return new Alert(parts[0].trim(), severity, parts[2].trim());
    }

    public List<Alert> parseAll(List<String> lines) {
        List<Alert> alerts = new ArrayList<>();
        for (String line : lines) {
            if (line.isBlank() || line.startsWith("#")) {
                continue;
            }
            alerts.add(parse(line));
        }
        return alerts;
    }
}
