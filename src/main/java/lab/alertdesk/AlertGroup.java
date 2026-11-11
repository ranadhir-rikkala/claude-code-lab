package lab.alertdesk;

public record AlertGroup(Alert first, int count, Severity highest) {

    public static AlertGroup of(Alert alert) {
        return new AlertGroup(alert, 1, alert.severity());
    }

    public AlertGroup add(Alert alert) {
        Severity max = alert.severity().compareTo(highest) > 0 ? alert.severity() : highest;
        return new AlertGroup(first, count + 1, max);
    }
}
