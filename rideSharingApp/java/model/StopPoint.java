package model;

public class StopPoint {
    private String streetNumber;
    private String streetName;
    private String suburb;

    public StopPoint(String streetNumber, String streetName, String suburb) {
        this.streetNumber = streetNumber;
        this.streetName = streetName;
        this.suburb = suburb;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public String getStreetName() {
        return streetName;
    }

    public String getSuburb() {
        return suburb;
    }

    @Override public String toString() {
        return String.format("%s %s", streetNumber, streetName, suburb);
    }

    @Override public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (!(o instanceof StopPoint)) {
            return false;
        }

        StopPoint other = (StopPoint) o;

        if (!(streetNumber.equals(other.streetNumber)) ||
                !(streetName.equals(other.streetName)) ||
                !(suburb.equals(other.suburb))) {
            return false;
        }
        return true;
    }

    @Override public int hashCode() {
        int result = 17;

        result = 31 * result + streetNumber.hashCode();
        result = 31 * result + streetName.hashCode();
        result = 31 * result + suburb.hashCode();

        return result;
    }
}
