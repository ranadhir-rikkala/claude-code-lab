package lab.alertdesk;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class App {

    public static void main(String[] args) throws IOException {
        Path input = Path.of(args.length > 0 ? args[0] : "data/alerts.txt");
        List<Alert> alerts = new AlertParser().parseAll(Files.readAllLines(input));
        List<AlertGroup> ranked = new Triage().rank(new Deduper().group(alerts));

        System.out.printf("%-10s %-9s %5s  %s%n", "SERVICE", "SEVERITY", "COUNT", "MESSAGE");
        for (AlertGroup group : ranked) {
            System.out.printf("%-10s %-9s %5d  %s%n",
                    group.first().service(), group.highest(), group.count(), group.first().message());
        }
        System.out.printf("%n%d alerts -> %d groups%n", alerts.size(), ranked.size());
    }
}
