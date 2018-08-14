package model;

import javafx.beans.property.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TripEvent {

    private List<StopPointEvent> stopPoints;
    private StringProperty direction = new SimpleStringProperty();
    private StringProperty date = new SimpleStringProperty();
    private Vehicle vehicle;
    private BooleanProperty shared = new SimpleBooleanProperty();
    private StringProperty routeName = new SimpleStringProperty();
    private IntegerProperty availableSeats = new SimpleIntegerProperty();
    private StringProperty stopPointProperty = new SimpleStringProperty();
    private String driverID;
    private List<String> bookings = new ArrayList<>();
    private IntegerProperty numBooked = new SimpleIntegerProperty(0);

    public TripEvent(List<StopPointEvent> stopPoints, TripDirection direction, LocalDate date, Vehicle vehicle, String routeName, String driverID) {
        this.stopPoints = stopPoints;
        this.direction.set(direction.toString());
        this.date.set(date.toString());
        this.vehicle = vehicle;
        this.routeName.set(routeName);
        this.driverID = driverID;

        this.availableSeats.set(0); // none available by default
        this.shared.set(false); // initially a ride is not shared

        String s = "";
        for (StopPointEvent stopPointEvent : stopPoints) {
            s += stopPointEvent.toString() + ",  " + stopPointEvent.getStopPoint().getSuburb() + ": " + stopPointEvent.getStopTime() + "\n";
        }
        stopPointProperty.set(s);
    }

    public List<StopPointEvent> getStopPoints() {
        return stopPoints;
    }

    public String getDirection() {
        return direction.get();
    }

    public StringProperty directionProperty() {
        return direction;
    }

    public String getDate() {
        return date.get();
    }

    public StringProperty dateProperty() {
        return date;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public boolean isShared() {
        return shared.get();
    }

    public BooleanProperty sharedProperty() {
        return shared;
    }

    public void setShared(boolean shared) {
        this.shared.set(shared);
    }

    public String getRouteName() {
        return routeName.get();
    }

    public StringProperty routeNameProperty() {
        return routeName;
    }

    public int getAvailableSeats() {
        return availableSeats.get();
    }

    public IntegerProperty availableSeatsProperty() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats.set(availableSeats);
    }

    public String getStopPointProperty() {
        return stopPointProperty.get();
    }

    public StringProperty stopPointPropertyProperty() {
        return stopPointProperty;
    }

    public String getDriverID() {
        return driverID;
    }

    public List<String> getBookings() {
        return bookings;
    }

    public int getNumBooked() {
        return numBooked.get();
    }

    public IntegerProperty numBookedProperty() {
        return numBooked;
    }

    public void addBooking(String id) {
        bookings.add(id);
        numBooked.set(numBooked.get() + 1);
        System.out.println(numBooked);
    }

    public String toString() {
        String s = "";
        for (StopPointEvent stopPointEvent : stopPoints) {
            s += stopPointEvent.toString() + " " + stopPointEvent.getStopPoint().getSuburb() + " ";
        }

        return s;
    }
}
