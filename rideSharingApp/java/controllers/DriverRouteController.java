package controllers;

import data.RSS;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import model.App;
import model.Session;
import model.StopPoint;
import model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by jth102 on 18/05/17.
 */
public class DriverRouteController {
    RSS rss;
    User user;

    @FXML private TextField streetNumberEntry;
    @FXML private TextField suburbEntry;
    @FXML private TextField streetNameEntry;
    @FXML private Label stopPointResponseLabel;
    @FXML private Label routeResponseLabel;
    @FXML private ListView<StopPoint> stopPointListView;
    @FXML private ListView<StopPoint> routeStopPointListView;
    @FXML private TextField stopPointSearchEntry;
    @FXML private TextField routeNameEntry;

    private ObservableList<StopPoint> stopPointObservableList;
    private ObservableList<StopPoint> routeStopPointObservableList;

    @FXML public void initialize() {
        rss = RSS.getInstance();
        user = Session.getInstance().getCurrentUser();

        setupStopPointView();
        setupRouteStopPointView();
    }

    @FXML private void addStopPointButtonAction() {
        String streetNumber = streetNumberEntry.getText();
        String streetName = streetNameEntry.getText();
        String suburb = suburbEntry.getText();

        if (rss.addStopPoint(streetNumber, streetName, suburb)) {
            stopPointResponseLabel.setTextFill(Color.GREEN);
            stopPointResponseLabel.setText(" Stop-point created!");
            stopPointObservableList.add(new StopPoint(streetNumber, streetName, suburb));
            stopPointListView.refresh();
            App.save(rss);
        } else {
            stopPointResponseLabel.setTextFill(Color.RED);
            stopPointResponseLabel.setText(" This stop-point already exists");
        }
    }

    @FXML private void createRouteButtonAction() {
        String routeName = routeNameEntry.getText();

        if (rss.addRoute(user.getId(), routeName, routeStopPointObservableList.stream().collect(Collectors.toList()))) {
            routeResponseLabel.setTextFill(Color.GREEN);
            routeResponseLabel.setText(" Created route!");
            routeNameEntry.clear();
            routeStopPointObservableList.clear();
            App.save(rss);
        } else {
            routeResponseLabel.setTextFill(Color.RED);
            routeResponseLabel.setText(" Enter a route name and at least one stop-point");
        }
    }

    @FXML private void addToRouteButtonAction() {
        routeStopPointObservableList.addAll(stopPointListView.getSelectionModel().getSelectedItems());
        routeStopPointListView.refresh();
    }

    private void setupStopPointView() {
        List<StopPoint> stopPointList;
        stopPointListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        stopPointList = new ArrayList<>(rss.getStopPointSet());
        stopPointObservableList = FXCollections.observableList(stopPointList);

        FilteredList<StopPoint> stopPointFilteredList = new FilteredList<StopPoint>(stopPointObservableList, e -> true);
        stopPointSearchEntry.setOnKeyReleased(e -> {
            stopPointSearchEntry.textProperty().addListener(((observable, oldValue, newValue) -> {
                stopPointFilteredList.setPredicate((Predicate<? super StopPoint>) stopPoint -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    if (stopPoint.toString().toLowerCase().contains(newValue)) {
                        return true;
                    }
                    return false;
                });
            }));
        });
        SortedList<StopPoint> stopPointSortedList = new SortedList<StopPoint>(stopPointFilteredList);
        stopPointListView.setItems(stopPointSortedList);
    }

    private void setupRouteStopPointView() {
        routeStopPointObservableList = FXCollections.observableArrayList();
        routeStopPointListView.setItems(routeStopPointObservableList);
    }
}
