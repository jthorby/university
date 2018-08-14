package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class StopPointEvent {

    private StopPoint stopPoint;
    private StringProperty stopTime = new SimpleStringProperty();

    public StopPointEvent(StopPoint stopPoint, String stopTime) {
        this.stopPoint = stopPoint;
        this.stopTime.set(stopTime);
    }

    public String getStopTime() {
        return stopTime.get();
    }

    public StringProperty stopTimeProperty() {
        return stopTime;
    }

    public void setStopTime(String stopTime) {
        this.stopTime.set(stopTime);
    }

    public String toString() {
        return stopPoint.toString();
    }

    public StopPoint getStopPoint() {
        return stopPoint;
    }
}
