package lab.alertdesk;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;

public final class App {

    private static final String MIN_SEVERITY = "--min-severity";

    public static void main(String[] args) throws IOException {
        Path input;
        Severity min;
        try {
            input = Path.of(inputPath(args));
            min = parseMinSeverity(args);
        } catch (IllegalArgumentException e) {
            System.err.println("error: " + e.getMessage());
            System.err.println("usage: run.sh [alerts-file] [--min-severity INFO|WARN|CRITICAL]");
            System.exit(2);
            return;
        }
        List<Alert> alerts = new AlertParser().parseAll(Files.readAllLines(input));
        Triage triage = new Triage();
        List<AlertGroup> ranked = triage.prioritize(triage.atLeast(new Deduper().group(alerts), min));

        System.out.printf("%-10s %-9s %5s  %s%n", "SERVICE", "SEVERITY", "COUNT", "MESSAGE");
        for (AlertGroup group : ranked) {
            System.out.printf("%-10s %-9s %5d  %s%n",
                    group.first().service(), group.highest(), group.count(), group.first().message());
        }
        System.out.printf("%n%d alerts -> %d groups%n", alerts.size(), ranked.size());
    }

    public static Severity parseMinSeverity(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (!args[i].equals(MIN_SEVERITY)) {
                continue;
            }
            if (i + 1 >= args.length) {
                throw new IllegalArgumentException(MIN_SEVERITY + " needs a value: INFO, WARN or CRITICAL");
            }
            String value = args[i + 1].trim().toUpperCase(Locale.ROOT);
            try {
                return Severity.valueOf(value);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Unknown severity '" + args[i + 1]
                        + "'; expected INFO, WARN or CRITICAL", e);
            }
        }
        return Severity.INFO;
    }

    static String inputPath(String[] args) {
        String path = null;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals(MIN_SEVERITY)) {
                i++;
            } else if (args[i].startsWith("-")) {
                throw new IllegalArgumentException("Unknown option '" + args[i] + "'");
            } else if (path != null) {
                throw new IllegalArgumentException("Expected one alerts file but got '" + path
                        + "' and '" + args[i] + "'");
            } else {
                path = args[i];
            }
        }
        return path != null ? path : "data/alerts.txt";
    }
}
