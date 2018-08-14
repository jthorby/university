package controllers;

import data.RSS;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import model.*;

public class ProfileOverviewController {
    private Session session;
    private RSS rss;
    private User user;

    @FXML public void initialize() {
        rss = RSS.getInstance();
        session = Session.getInstance();
        user = session.getCurrentUser();
    }
}
