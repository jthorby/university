package controllers;

import data.RSS;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import model.Session;
import model.User;
import model.StopPoint;

/**
 * Created by jth102 on 11/05/17.
 */
public class driverOverviewController {
    private RSS rss;
    private User user;

    @FXML public void initialize() {
        rss = RSS.getInstance();
        user = Session.getInstance().getCurrentUser();
    }

    @FXML private void createStopPointButtonAction() {

    }

    @FXML private void addStopPointButtonAction() {

    }
}
