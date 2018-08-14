package controllers;

import data.RSS;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Session;
import model.User;
import model.Vehicle;
import model.ViewNavigator;

import java.time.LocalDate;

public class MasterController {
    private Stage stage;
    private RSS rss;
    private static Session session;
    private User user;

    @FXML private Pane content;
    @FXML private Label nameLabel;
    @FXML private Label currentVehicleLabel;

    @FXML public void initialize() {
        rss = RSS.getInstance();
        session = Session.getInstance();
        user = session.getCurrentUser();
        loadUserContent();
    }

    @FXML private void logoutMenuAction() {
        session.closeSession();
        ViewNavigator.toLogin(stage);
    }

    @FXML private void profileButtonAction() {
        ViewNavigator.toProfileLanding();
    }

    @FXML private void driveButtonAction() {
        ViewNavigator.toDriverLanding();
    }

    @FXML private void rideButtonAction() {
        ViewNavigator.toRideLanding();
    }

    public void setView(Node node) {
        content.getChildren().setAll(node);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void loadUserContent() {
        nameLabel.textProperty().bind(session.userNameProperty());

        if (user.getCurrentVehicle() != null) {
            session.setCurrentVehicleProperty(user.getCurrentVehicle());
        }
        currentVehicleLabel.textProperty().bind(session.currentVehicleProperty());
    }
}
