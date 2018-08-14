package controllers;

import data.RSS;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import model.*;

import java.time.LocalDate;

public class DriverRegistrationController {
    RSS rss;
    Session session;
    User user;

    @FXML private SplitMenuButton licenseType;
    @FXML private TextField licenseNumber;
    @FXML private DatePicker issueDate;
    @FXML private DatePicker expiryDate;
    @FXML private Label responseLabel;
    @FXML private Pane content;

    @FXML public void initialize() {
        rss = RSS.getInstance();
        session = Session.getInstance();
        user = session.getCurrentUser();
    }

    @FXML private void registerAction() {
        String licenseTypeG = licenseType.getText(); // "G" is for "Given"
        String licenseNumberG = licenseNumber.getText();
        LocalDate issueDateG = issueDate.getValue();
        LocalDate expiryDateG = expiryDate.getValue();

        if (!(licenseTypeG.isEmpty() || licenseNumberG.isEmpty() ||
                issueDateG == null || expiryDateG == null)) {
            if (licenseNumberG.matches("[A-Z0-9]*")) {
                System.out.println(user.getId());
                rss.addLicense(user.getId(), new License(licenseTypeG, licenseNumberG, issueDateG, expiryDateG));
                user.setRegisteredDriver(true);
                App.save(rss);
                responseLabel.setText("Successfully registered!");

                ViewNavigator.toDriverLanding();
            } else {
                responseLabel.setText("Please Enter a valid license number");
            }
        } else {
            responseLabel.setText("Please enter all fields");
        }
    }

    @FXML private void restrictedRadioAction() {
        licenseType.setText("Restricted");
    }

    @FXML private void fullRadioAction() {
        licenseType.setText("Full");
    }

    @FXML private void full2RadioAction() {
        licenseType.setText("Full (Longer than 2 years)");
    }
}
