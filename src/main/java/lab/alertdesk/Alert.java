package lab.alertdesk;

public record Alert(String service, Severity severity, String message) {
}
