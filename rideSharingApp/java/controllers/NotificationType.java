package controllers;

/**
 * Created by Justin on 2/06/2017.
 */
public enum NotificationType {
    WOF("Warrant of fitness expiring soon"),
    REGO("Registration expiring soon"),
    LICENSE("License expiring soon");

    private final String text;

    NotificationType(final String text) {
        this.text = text;
    }

    @Override public String toString() {
        return text;
    }
}
