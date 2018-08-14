package controllers;

import data.RSS;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.App;
import model.Session;
import model.User;
import model.ViewNavigator;

import java.io.IOException;
import java.time.LocalDate;

public class LoginController {
    private Stage stage;
    private RSS rss;
    private Session session;
    private User user;

    // Login stuff
    @FXML
    private TextField existingID;
    @FXML
    private TextField existingPassword;
    @FXML
    private Label loginResponse;

    // Account creation stuff
    @FXML
    private TextField id;
    @FXML
    private TextField email;
    @FXML
    private TextField mobileNumber;
    @FXML
    private TextField name;
    @FXML
    private TextField address;
    @FXML
    private TextField password;
    @FXML
    private TextField rePassword;
    @FXML
    private Label createResponse;

    public void initialize() {
        rss = RSS.getInstance();
        session = Session.getInstance();
    }

    @FXML
    private void loginButtonAction(ActionEvent actionEvent) throws IOException {
        if (userValid()) {
            ViewNavigator.toMaster(stage);
        } else {
            loginResponse.setText("Invalid UC ID or Password");
        }
    }

    private boolean userValid() throws IOException {
        boolean valid = false;
        String rcvdID = existingID.getText();
        String rcvdPassword = existingPassword.getText();
        User user = rss.getUsersMap().get(rcvdID);

        if (user != null) {
            if (user.getPassword().equals(rcvdPassword)) {
                valid = true;
                login(user);
            }
        }
        return valid;
    }

    @FXML
    private void createAccountButtonAction(ActionEvent actionEvent) throws IOException {
        String rID = id.getText();
        String rEmail = email.getText();
        String rmNumber = mobileNumber.getText();
        String rName = name.getText();
        String rAddress = address.getText();
        String rPassword = password.getText();
        String rRePassword = rePassword.getText();

        if (!(rID.isEmpty() || rEmail.isEmpty() || rmNumber.isEmpty() || rName.isEmpty() ||
                rAddress.isEmpty() || rPassword.isEmpty() || rRePassword.isEmpty())) { // if any field is not empty
            if (rID.matches("[0-9]*")) {
                if (rEmail.matches("[a-zA-Z0-9]*@(uclive.ac.nz|canterbury.ac.nz)")) { // check for uc email
                    if (rPassword.equals(rRePassword)) { // if passwords match
                        User user = new User(rID, rName, rEmail, rmNumber, rAddress, rPassword);
                        if (rss.addUser(rID, user)) {
                            login(user);
                        } else {
                            createResponse.setText("A user with this ID already exists");
                        }
                    } else {
                        createResponse.setText("Passwords must match");
                    }
                } else {
                    createResponse.setText("Please enter a valid UC email address");
                }
            } else {
                createResponse.setText("Please enter a valid UC ID");
            }
        } else {
            createResponse.setText("Please fill out all fields");
        }
    }

    private void login(User user) throws IOException {
        App.save(rss);
        session.setCurrentUser(user);
        this.user = user;
        notifications();
        ViewNavigator.toMaster(stage);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void notifications() {
        if (user.getCurrentVehicle() != null) {
            LocalDate license = rss.getLicenseMap().get(user.getId()).getExpiryDate();
            LocalDate wof = user.getCurrentVehicle().getWofExpiry();
            LocalDate rego = user.getCurrentVehicle().getRegoExpiry();
            LocalDate month = LocalDate.now().plusMonths(1);
            LocalDate week = LocalDate.now().plusWeeks(1);
            LocalDate week2 = LocalDate.now().plusWeeks(2);

            int status;
            String message = "";
            for (NotificationType notificationType : NotificationType.values()) {
                status = user.notificationStatus(notificationType);
                if (status <= 3) {
                    if (notificationType == NotificationType.LICENSE) {
                        boolean notify = false;
                        if (status == 0) {
                            if (!license.isAfter(month)) {
                                notify = true;
                                user.setLicenseNotification(status + 1);
                            }
                        } else if (status == 1) {
                            if (!license.isAfter(week)) {
                                notify = true;
                                user.setLicenseNotification(status + 1);
                            }
                        } else if (status == 2) {
                            if (!license.isAfter(week2)) {
                                notify = true;
                                user.setLicenseNotification(status + 1);
                            }
                        }
                        if (notify) {
                            message += notificationType.toString() + "\n";
                        }
                    } else if (notificationType == NotificationType.WOF) {
                        boolean notify = false;
                        if (status == 0) {
                            if (!wof.isAfter(month)) {
                                notify = true;
                                user.setWofNotification(status + 1);
                            }
                        } else if (status == 1) {
                            if (!wof.isAfter(week)) {
                                notify = true;
                                user.setWofNotification(status + 1);
                            }
                        } else if (status == 2) {
                            if (!wof.isAfter(week2)) {
                                notify = true;
                                user.setWofNotification(status + 1);
                            }
                        }
                        if (notify) {
                            message += notificationType.toString() + "\n";
                        }
                    } else if (notificationType == NotificationType.REGO) {
                        boolean notify = false;
                        if (status == 0) {
                            if (!rego.isAfter(month)) {
                                notify = true;
                                user.setRegoNotification(status + 1);
                            }
                        } else if (status == 1) {
                            if (!rego.isAfter(week)) {
                                notify = true;
                                user.setRegoNotification(status + 1);
                            }
                        } else if (status == 2) {
                            if (!rego.isAfter(week2)) {
                                notify = true;
                                user.setRegoNotification(status + 1);
                            }
                        }
                        if (notify) {
                            message += notificationType.toString() + "\n";
                        }
                    }
                }
            }
            if (!message.equals("")) {
                Notification.showPopupMessage(message, stage);
            }
            App.save(rss);
        }
    }
}
