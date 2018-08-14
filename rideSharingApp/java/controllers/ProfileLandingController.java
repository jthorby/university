package controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import model.ViewNavigator;

import java.util.List;

/**
 * Created by Justin on 6/05/2017.
 */
public class ProfileLandingController {
    @FXML private Pane profileContentPane;

    @FXML public void initialize() {

    }

    @FXML private void overviewButtonAction() {
        ViewNavigator.toProfileOverview();
    }

    public void setView(Node node) {
        profileContentPane.getChildren().setAll(node);
    }
}
