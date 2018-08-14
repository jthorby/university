package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by Justin on 30/04/2017.
 */
public class Session {
    private static Session instance;

    private User currentUser;
    private static StringProperty currentVehicle;
    private static StringProperty userName;

    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
            currentVehicle = new SimpleStringProperty("No current vehicle");
            userName = new SimpleStringProperty("");
        }
        return instance;
    }

    public void setCurrentUser(User user) {
        currentUser = user;
        userName.set(user.getName());
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentVehicleProperty(Vehicle vehicle) {
        currentVehicle.set(vehicle.toString());
    }

    public StringProperty currentVehicleProperty() {
        return currentVehicle;
    }

    public StringProperty userNameProperty() {
        return userName;
    }

    public void closeSession() {
        instance = null;
    }
}
