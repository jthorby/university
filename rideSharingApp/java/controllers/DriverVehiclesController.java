package controllers;

import data.RSS;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import model.App;
import model.Session;
import model.User;
import model.Vehicle;

import java.time.LocalDate;
import java.util.Date;

public class DriverVehiclesController {
    private RSS rss;
    private Session session;
    private User user;
    private ObservableList<Vehicle> vehicleObservableList;

    @FXML TextField type;
    @FXML TextField model;
    @FXML TextField colour;
    @FXML TextField plate;
    @FXML TextField year;
    @FXML TextField seatsNum;
    @FXML DatePicker wofExpiryDatePicker;
    @FXML DatePicker regoExpiryDatePicker;
    @FXML Label responseLabel;
    @FXML private ListView<Vehicle> vehiclesList;

    @FXML public void initialize() {
        rss = RSS.getInstance();
        session = Session.getInstance();
        user = session.getCurrentUser();

        vehicleObservableList = FXCollections.observableArrayList(rss.getUsersVehicles(user));
        vehiclesList.setItems(vehicleObservableList);
    }

    @FXML public void addVehicleAction() {
        String typeS = type.getText();
        String modelS = model.getText();
        String colourS = colour.getText();
        String plateS = plate.getText();
        String yearS = year.getText();
        String seatsNumS = seatsNum.getText();
        LocalDate wof = wofExpiryDatePicker.getValue();
        LocalDate rego = regoExpiryDatePicker.getValue();

        if (!(typeS.isEmpty() || modelS.isEmpty() || colourS.isEmpty() ||
              plateS.isEmpty() || yearS.isEmpty() || seatsNumS.isEmpty())) {
            if (yearS.matches("[0-9]*")) {
                int yearI = Integer.parseInt(yearS);
                int seatsNumI = Integer.parseInt(seatsNumS);

                String response = String.format("Registered %d %s!", yearI, modelS);
                responseLabel.setTextFill(Paint.valueOf("green"));
                responseLabel.setText(response);

                Vehicle vehicle = new Vehicle(typeS, modelS, colourS, plateS, yearI, seatsNumI, wof, rego);
                rss.addVehicle(user.getId(), plateS, vehicle);
                vehicleObservableList.add(vehicle);
                vehiclesList.setItems(vehicleObservableList);
                App.save(rss);
            } else {
                responseLabel.setTextFill(Paint.valueOf("red"));
                responseLabel.setText("Please enter a valid year");
            }
        } else {
            responseLabel.setTextFill(Paint.valueOf("red"));
            responseLabel.setText("Please enter all fields");
        }
    }

    @FXML private void currentVehicleButtonAction() {
        Vehicle selected = vehiclesList.getSelectionModel().getSelectedItem();

        if (selected != null) {
            user.setCurrentVehicle(selected);
            session.setCurrentVehicleProperty(selected);
            App.save(rss);
        }
    }
}
