package controllers;


import data.RSS;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.stage.Popup;
import javafx.stage.Stage;
import model.*;

import javax.xml.soap.Text;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DriverTripsController {
    private RSS rss;
    private Session session;
    private User user;
    private ObservableList<StopPointEvent> spEvents = FXCollections.observableArrayList();
    private ObservableList<TripEvent> tripEvents;
    private List<LocalDate> occurrences;
    private List<CheckBox> daysBoxes = new ArrayList<>();

    @FXML private HBox daysOfWeekBox;
    @FXML private CheckBox recurringBox;
    @FXML private CheckBox fridayBox;
    @FXML private CheckBox thursdayBox;
    @FXML private CheckBox sundayBox;
    @FXML private CheckBox saturdayBox;
    @FXML private DatePicker expiryDatePicker;
    @FXML private CheckBox wednesdayBox;
    @FXML private CheckBox tuesdayBox;
    @FXML private CheckBox mondayBox;
    @FXML private Label expiryDateLabel;
    @FXML private ComboBox<TripDirection> directionBox;
    @FXML private ComboBox<Route> routeBox;
    @FXML private TableView<StopPointEvent> stopPointTable;
    @FXML private TableColumn<StopPointEvent, String> stopPointColumn;
    @FXML private TableColumn<StopPointEvent, String> stopTimeColumn;
    @FXML private TableView<TripEvent> myTripsTable;
    @FXML private TableColumn<TripEvent, String> routeColumn;
    @FXML private TableColumn<TripEvent, String> directionColumn;
    @FXML private TableColumn<TripEvent, String> dateColumn;
    @FXML private TableColumn<TripEvent, String> sharedColumn;
    @FXML private TableColumn<TripEvent, Integer> bookingsColumn;
    @FXML private TextField seatsAvailableEntry;

    @FXML public void initialize() {
        daysOfWeekBox.setSpacing(35);
        rss = RSS.getInstance();
        session = Session.getInstance();
        user = session.getCurrentUser();

        setupRouteBox();
        setupDaysBoxes();
        setupDirectionBox();
        setupMyTrips();
        stopPointTable.setEditable(true);
    }

    @FXML private void toggleDaysOfWeek() {
        mondayBox.setDisable(!mondayBox.isDisable());
        tuesdayBox.setDisable(!tuesdayBox.isDisable());
        wednesdayBox.setDisable(!wednesdayBox.isDisable());
        thursdayBox.setDisable(!thursdayBox.isDisable());
        fridayBox.setDisable(!fridayBox.isDisable());
        saturdayBox.setDisable(!saturdayBox.isDisable());
        sundayBox.setDisable(!sundayBox.isDisable());
        expiryDateLabel.setDisable(!expiryDateLabel.isDisable());
        expiryDatePicker.setDisable(!expiryDatePicker.isDisable());
    }

    @FXML private void routeBoxAction() {
        populateStopPointTable(routeBox.getValue());
    }

    @FXML private void createTripAction() {
        if (expiryDatePicker.getValue() != null) {
            occurrences = new ArrayList<>();

            if (!recurringBox.isSelected()) { // only add today as an occurrence
                occurrences.add(LocalDate.now());
            } else {
                setOccurrences();
            }

            TripDirection direction = directionBox.getValue();
            Vehicle vehicle = user.getCurrentVehicle();

            for (LocalDate date : occurrences) {
                rss.addTrip(user.getId(), spEvents, direction, date, vehicle, routeBox.getSelectionModel().getSelectedItem().getName());
            }

            updateMyTrips();

            App.save(rss);
        }
    }

    @FXML private void shareTrip() {
        TripEvent tripSelected = myTripsTable.getSelectionModel().getSelectedItem();

        if (tripSelected != null) {
            int actualNumSeats = tripSelected.getVehicle().getSeatsNum();
            int givenSeatsNum = Integer.parseInt(seatsAvailableEntry.getText());

            if (givenSeatsNum < actualNumSeats) {
                tripSelected.setAvailableSeats(givenSeatsNum);
                tripSelected.setShared(true);
            }
        }

        App.save(rss);
    }

    private void setupRouteBox() {
        ObservableList<Route> routes = FXCollections.observableArrayList(rss.getUsersRoutes(user.getId()));
        routeBox.setItems(routes);
    }

    private void setupDirectionBox() {
        ObservableList<TripDirection> directions = FXCollections.observableArrayList(TripDirection.FROM_UNI, TripDirection.TO_UNI);
        directionBox.setItems(directions);
        directionBox.getSelectionModel().selectFirst();
    }

    private void setupDaysBoxes() {
        daysBoxes.add(mondayBox);
        daysBoxes.add(tuesdayBox);
        daysBoxes.add(wednesdayBox);
        daysBoxes.add(thursdayBox);
        daysBoxes.add(fridayBox);
        daysBoxes.add(saturdayBox);
        daysBoxes.add(sundayBox);
    }

    private void populateStopPointTable(Route route) {
        ObservableList<StopPoint> stopPoints = FXCollections.observableArrayList(route.getStopPoints());
        spEvents.clear();

        for (StopPoint stopPoint : stopPoints) {
            spEvents.add(new StopPointEvent(stopPoint, "00:00"));
        }

        stopPointColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().toString()));

        stopTimeColumn.setCellValueFactory(new PropertyValueFactory<>("stopTime"));
        stopTimeColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        stopTimeColumn.setOnEditCommit(
                t -> (t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setStopTime(t.getNewValue())
        );

        stopPointTable.setItems(spEvents);
    }

    private void setOccurrences() {
        boolean notDone = true;
        LocalDate expiry = expiryDatePicker.getValue();
        LocalDate currentDate = LocalDate.now();
        DayOfWeek currentWeekDay = currentDate.getDayOfWeek();
        boolean firstIteration = true;
        boolean stillWorking = true;

        while (notDone) {
            DayOfWeek iteratingDay = DayOfWeek.MONDAY;
            for (CheckBox dayBox : daysBoxes) {

                if (firstIteration && stillWorking) {
                    if (iteratingDay.getValue() < currentWeekDay.getValue() - 1) {
                    } else {
                        stillWorking = false;
                    }
                } else {
                    if (!currentDate.isAfter(expiry)) {

                        if (dayBox.isSelected()) {
                            occurrences.add(currentDate);
                        }
                        currentDate = currentDate.plusDays(1);

                    } else {
                        notDone = false;
                    }
                }
                iteratingDay = iteratingDay.plus(1);
            }
            firstIteration = false;
        }
    }

    private void setupMyTrips() {
        tripEvents = FXCollections.observableArrayList(rss.getUsersTrips(user.getId()));
        routeColumn.setCellValueFactory(new PropertyValueFactory<>("stopPointProperty"));
        directionColumn.setCellValueFactory(new PropertyValueFactory<>("direction"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        sharedColumn.setCellValueFactory(new PropertyValueFactory<>("shared"));
        bookingsColumn.setCellValueFactory(new PropertyValueFactory<>("numBooked"));
        myTripsTable.setItems(tripEvents);
    }

    private void updateMyTrips() {
        tripEvents = FXCollections.observableArrayList(rss.getUsersTrips(user.getId()));
        myTripsTable.setItems(tripEvents);
        myTripsTable.refresh();
    }
}
