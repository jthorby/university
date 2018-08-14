package model;

public enum TripDirection {
    TO_UNI("To University"),
    FROM_UNI("From University");

    private final String text;

    TripDirection(final String text) {
        this.text = text;
    }

    @Override public String toString() {
        return text;
    }
}
