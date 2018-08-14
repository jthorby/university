package controllers;

import data.RSS;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Session;
import model.User;
import model.ViewNavigator;

public class DriverLandingController {
    private RSS rss;
    private Session session;
    private User user;

    @FXML private Pane mainPane;
    @FXML private Pane driverContentPane;
    @FXML private Button overviewButton;
    @FXML private Button addVehicleButton;

    @FXML public void initialize() {
        rss = RSS.getInstance();
        session = Session.getInstance();
        user = session.getCurrentUser();

        if (!(user.isRegisteredDriver())) {
            disableButtons();
        } else {
            enableButtons();
        }
    }

    @FXML private void overviewButtonAction() {
        ViewNavigator.toDriverOverview();
    }

    @FXML private void registerAction() {
        ViewNavigator.toDriverRegistration();
    }

    @FXML private void addVehicleAction() {
        ViewNavigator.toAddVehicle();
    }

    @FXML private void routeButtonAction() {
        ViewNavigator.toDriverRoutes();
    }

    @FXML private void tripsButtonAction() {
        ViewNavigator.toDriverTrips();
    }

    private void enableButtons() {
        overviewButton.setDisable(false);
        addVehicleButton.setDisable(false);
    }

    private void disableButtons() {
        overviewButton.setDisable(true);
        addVehicleButton.setDisable(true);
    }

    public void setView(Node node) {
        driverContentPane.getChildren().setAll(node);
    }
}
