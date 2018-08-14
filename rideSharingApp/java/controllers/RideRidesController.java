package controllers;

import data.RSS;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class RideRidesController {
    private Session session;
    private RSS rss;
    private User user;

    @FXML private TableView<TripEvent> ridesTable;
    @FXML private TableColumn<TripEvent, String> routeColumn;
    @FXML private TableColumn<TripEvent, String> directionColumn;
    @FXML private TableColumn<TripEvent, String> dateColumn;
    @FXML private TableColumn<TripEvent, String> seatsColumn;
    @FXML private TextField searchField;
    @FXML private TableView<RideDetail> detailsTable;
    @FXML private TableColumn<RideDetail, String> driverColumn;
    @FXML private TableColumn<RideDetail, String> yearColumn;
    @FXML private TableColumn<RideDetail, String> modelColumn;
    @FXML private TableColumn<RideDetail, String> ratingColumn;
    @FXML private TableView<TripEvent> myRidesTable;
    @FXML private TableColumn<TripEvent, String> myRouteColumn;
    @FXML private TableColumn<TripEvent, String> myDirectionColumn;
    @FXML private TableColumn<TripEvent, String> myDateColumn;
    @FXML private TableColumn<TripEvent, String> myStatusColumn;

    private ObservableList<TripEvent> rides;
    private ObservableList<TripEvent> myRides;

    @FXML public void initialize() {
        rss = RSS.getInstance();
        session = Session.getInstance();
        user = session.getCurrentUser();

        setupRidesTable();
        setupMyRidesTable();
    }

    @FXML public void bookSeatAction() {
        TripEvent selectedTrip = ridesTable.getSelectionModel().getSelectedItem();
        if (selectedTrip != null) {
            selectedTrip.setAvailableSeats(selectedTrip.getAvailableSeats() - 1);
            selectedTrip.addBooking(user.getId());
            updateRidesTable();
            ridesTable.setItems(rides);
            updateMyRidesTable();
            myRidesTable.setItems(myRides);
            App.save(rss);
        }
    }

    @FXML public void showDetailsAction() {
        TripEvent selected = ridesTable.getSelectionModel().getSelectedItem();
        ObservableList<RideDetail> detailObservableList = FXCollections.observableArrayList();
        User tripOwner = (rss.getUsersMap().get(selected.getDriverID()));
        RideDetail detail = new RideDetail(tripOwner.getName(), selected.getVehicle().getYear(), selected.getVehicle().getModel(), tripOwner.getRating());
        driverColumn.setCellValueFactory(new PropertyValueFactory<>("driver"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        modelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));
        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));
        detailObservableList.add(detail);
        detailsTable.setItems(detailObservableList);

    }

    private void setupRidesTable() {
        updateRidesTable();
        routeColumn.setCellValueFactory(new PropertyValueFactory<>("stopPointProperty"));
        directionColumn.setCellValueFactory(new PropertyValueFactory<>("direction"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        seatsColumn.setCellValueFactory(new PropertyValueFactory<>("availableSeats"));

        FilteredList<TripEvent> tripEventFilteredList = new FilteredList<TripEvent>(rides, e -> true);
        searchField.setOnKeyReleased(e -> {
            searchField.textProperty().addListener(((observable, oldValue, newValue) -> {
                tripEventFilteredList.setPredicate((Predicate<? super TripEvent>) trip -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    if (trip.toString().toLowerCase().contains(newValue)) {
                        return true;
                    }
                    return false;
                });
            }));
        });
        SortedList<TripEvent> tripEventSortedList= new SortedList<TripEvent>(tripEventFilteredList);
        ridesTable.setItems(tripEventSortedList);
    }

    private void updateRidesTable() {
        rides = FXCollections.observableArrayList();

        Map<String, List<TripEvent>> tripMap = rss.getTripMap();
        for (List<TripEvent> tripEvents : tripMap.values()) {
            for (TripEvent tripEvent : tripEvents) {
                if (tripEvent.isShared() && (tripEvent.getAvailableSeats() > 0)) {
                    rides.add(tripEvent);
                }
            }
        }
        ridesTable.refresh();
    }

    private void setupMyRidesTable() {
        updateMyRidesTable();
        myRouteColumn.setCellValueFactory(new PropertyValueFactory<>("stopPointProperty"));
        myDirectionColumn.setCellValueFactory(new PropertyValueFactory<>("direction"));
        myDateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        myRidesTable.setItems(myRides);
    }

    private void updateMyRidesTable() {
        myRides = FXCollections.observableArrayList();
        Map<String, List<TripEvent>> tripMap = rss.getTripMap();
        for (List<TripEvent> tripEvents : tripMap.values()) {
            for (TripEvent tripEvent : tripEvents) {
                if (tripEvent.getBookings().contains(user.getId())) {
                    myRides.add(tripEvent);
                }
            }
        }
    }
}

