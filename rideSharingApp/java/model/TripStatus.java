package model;

public enum TripStatus {
    DONE("Done"),
    BOOKED("Booked"),
    CANCELLED("Cancelled");

    private final String text;

    TripStatus(final String text) {
        this.text = text;
    }

    @Override public String toString() {
        return text;
    }
}
