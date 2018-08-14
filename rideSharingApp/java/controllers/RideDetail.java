package controllers;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class RideDetail {

    private StringProperty driver = new SimpleStringProperty();
    private IntegerProperty year = new SimpleIntegerProperty();
    private StringProperty model = new SimpleStringProperty();
    private IntegerProperty rating = new SimpleIntegerProperty();

    public RideDetail(String driver, int year, String model, int rating) {
        this.driver.set(driver);
        this.year.set(year);
        this.model.set(model);
        this.rating.set(rating);
    }

    public String getDriver() {
        return driver.get();
    }

    public StringProperty driverProperty() {
        return driver;
    }

    public int getYear() {
        return year.get();
    }

    public IntegerProperty yearProperty() {
        return year;
    }

    public String getModel() {
        return model.get();
    }

    public StringProperty modelProperty() {
        return model;
    }

    public int getRating() {
        return rating.get();
    }

    public IntegerProperty ratingProperty() {
        return rating;
    }
}
