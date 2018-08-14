package model;

import controllers.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class ViewNavigator {
    private static final String MASTER = "/master.fxml";
    private static final String LOGIN = "/login.fxml";

    private static final String DRIVER_LANDING = "/driverLanding.fxml";
    private static final String DRIVER_OVERVIEW = "/driverOverview.fxml";
    private static final String DRIVER_REGISTRATION = "/driverRegistration.fxml";
    private static final String DRIVER_ROUTES = "/driverRoutes.fxml";
    private static final String DRIVER_TRIPS = "/driverTrips.fxml";
    private static final String ADD_VEHICLE = "/myVehicles.fxml";

    private static final String RIDE_LANDING = "/rideLanding.fxml";
    private static final String RIDE_RIDES = "/rideRides.fxml";

    private static final String PROFILE_LANDING = "/profileLanding.fxml";
    private static final String PROFILE_OVERVIEW = "/profileOverview.fxml";

    private static MasterController masterController;
    private static ProfileLandingController profileLandingController;
    private static RideLandingController rideLandingController;
    private static DriverLandingController driverLandingController;

    private static void loadMainView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader();
            Node node = loader.load(ViewNavigator.class.getResourceAsStream(fxmlPath));

            if (fxmlPath.equals(DRIVER_LANDING)) {
                driverLandingController = loader.getController();
            }
            if (fxmlPath.equals(PROFILE_LANDING)) {
                profileLandingController = loader.getController();
            }

            if (fxmlPath.equals(RIDE_LANDING)) {
                rideLandingController = loader.getController();
            }

            masterController.setView(node);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void loadProfileSubView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader();
            Node node = loader.load(ViewNavigator.class.getResourceAsStream(fxmlPath));
            profileLandingController.setView(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadDriverSubView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader();
            Node node = loader.load(ViewNavigator.class.getResourceAsStream(fxmlPath));
            driverLandingController.setView(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadRideSubView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader();
            Node node = loader.load(ViewNavigator.class.getResourceAsStream(fxmlPath));
            rideLandingController.setView(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Pane loadLoginPane(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader();

        Pane loginPane = loader.load(App.class.getResourceAsStream(ViewNavigator.LOGIN));

        LoginController loginController = loader.getController();
        loginController.setStage(stage);

        return loginPane;
    }

    private static Pane loadMasterPane(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader();

        Pane masterPane = loader.load(App.class.getResourceAsStream(ViewNavigator.MASTER));

        masterController = loader.getController();
        masterController.setStage(stage);

        ViewNavigator.loadMainView(ViewNavigator.DRIVER_LANDING);

        return masterPane;
    }

    private static Scene createScene(Pane pane) {
        return new Scene(pane);
    }

    public static void toLogin(Stage stage) {
        try {
            stage.setScene(createScene(loadLoginPane(stage)));
            stage.show();
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void toMaster(Stage stage) {
        try {
            stage.setScene(createScene(loadMasterPane(stage)));
            stage.show();
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Main View options
    public static void toDriverLanding() {
        ViewNavigator.loadMainView(ViewNavigator.DRIVER_LANDING);
    }
    public static void toRideLanding() {
        ViewNavigator.loadMainView(ViewNavigator.RIDE_LANDING);
    }
    public static void toProfileLanding() {
        ViewNavigator.loadMainView(ViewNavigator.PROFILE_LANDING);
    }

    // My Profile view options
    public static void toProfileOverview() {
        ViewNavigator.loadProfileSubView(ViewNavigator.PROFILE_OVERVIEW);
    }

    // Driver view options
    public static void toDriverRegistration() {
        ViewNavigator.loadDriverSubView(ViewNavigator.DRIVER_REGISTRATION);
    }
    public static void toAddVehicle() {
        ViewNavigator.loadDriverSubView(ViewNavigator.ADD_VEHICLE);
    }
    public static void toDriverOverview() {
        ViewNavigator.loadDriverSubView(ViewNavigator.DRIVER_OVERVIEW);
    }
    public static void toDriverRoutes() {
        ViewNavigator.loadDriverSubView(ViewNavigator.DRIVER_ROUTES);
    }
    public static void toDriverTrips() {
        ViewNavigator.loadDriverSubView(ViewNavigator.DRIVER_TRIPS);
    }

    // Ride view options
    public static void toRides() {
        ViewNavigator.loadRideSubView(ViewNavigator.RIDE_RIDES);
    }
}
