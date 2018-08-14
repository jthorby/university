package controllers;

import data.RSS;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import model.Session;
import model.ViewNavigator;

public class RideLandingController {
    private RSS rss;
    private Session session;

    @FXML private Pane rideContentPane;

    public void initialize() {
        rss = RSS.getInstance();
        session = Session.getInstance();
    }

    @FXML public void toRidesAction() {
        ViewNavigator.toRides();
    }

    public void setView(Node node) {
        rideContentPane.getChildren().setAll(node);
    }
}