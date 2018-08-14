package model;

import controllers.NotificationType;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class User {

    private String id;
    private String name;
    private String email;
    private String mobileNumber;
    private String address;
    private String password;
    private boolean isRegisteredDriver;
    private Vehicle currentVehicle;
    private int rating;
    private int wofNotification = 0;
    private int licenseNotification = 0;
    private int regoNotification = 0;

    public User(String id, String name, String email, String mobileNumber,
                String address, String password) {

        this.id = id;
        this.name = name;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.address = address;
        this.password = password;
        this.rating = 0;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getPassword() {
        return password;
    }

    public boolean isRegisteredDriver() {
        return isRegisteredDriver;
    }

    public void setRegisteredDriver(boolean registeredDriver) {
        isRegisteredDriver = registeredDriver;
    }

    public Vehicle getCurrentVehicle() {
        return currentVehicle;
    }

    public void setCurrentVehicle(Vehicle currentVehicle) {
        this.currentVehicle = currentVehicle;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getWofNotification() {
        return wofNotification;
    }

    public void setWofNotification(int wofNotification) {
        this.wofNotification = wofNotification;
    }

    public int getLicenseNotification() {
        return licenseNotification;
    }

    public void setLicenseNotification(int licenseNotification) {
        this.licenseNotification = licenseNotification;
    }

    public int getRegoNotification() {
        return regoNotification;
    }

    public void setRegoNotification(int regoNotification) {
        this.regoNotification = regoNotification;
    }

    public int notificationStatus(NotificationType notificationType) {
        int status = -1;
        switch (notificationType) {
            case LICENSE: status = licenseNotification; break;
            case WOF: status = wofNotification; break;
            case REGO: status = regoNotification; break;
        }
        return status;
    }
}
