package data;

import model.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;


public class RSS {
    private static RSS instance = null;
    private Map<String, User> usersMap; // Keys: user ID
    private Map<String, License> licenseMap; // Keys: user ID
    private Map<String, Map<String, Vehicle>> vehicleMap; // Keys: User ID, Plate number
    private Map<String, List<Route>> routeMap; // Keys: User ID
    private Map<String, List<TripEvent>> tripMap; // Keys: User ID
    private Set<StopPoint> stopPointSet;

    private RSS() {
        usersMap = new HashMap<>();
        licenseMap = new HashMap<>();
        vehicleMap = new HashMap<>();
        routeMap = new HashMap<>();
        stopPointSet = new HashSet<>();
        tripMap = new HashMap<>();
    }

    public void setInstance(RSS instance) {
        this.instance = instance;
    }

    public static RSS getInstance() {
        if (instance == null) {
            instance = new RSS();
        }
        return instance;
    }

    public Map<String, User> getUsersMap() {
        return usersMap;
    }

    public boolean addUser(String ID, User user) {
        int prevSize = usersMap.size();
        usersMap.putIfAbsent(ID, user);

        return usersMap.size() > prevSize;
    }

    public void addLicense(String ID, License license) {
        licenseMap.put(ID, license);
    }

    public void addVehicle(String ID, String plate, Vehicle vehicle) {
        Map<String, Vehicle> userVehicleMap;
        userVehicleMap = vehicleMap.get(ID);

        if (userVehicleMap == null) {
            userVehicleMap = new HashMap<>();
            vehicleMap.put(ID, userVehicleMap);
        }

        userVehicleMap.put(plate, vehicle);
    }

    public List getUsersVehicles(User user) {
        Map map = vehicleMap.get(user.getId());
        List<Vehicle> vehicles;

        if (map == null) {
            vehicles = new ArrayList<>();
        } else {
            vehicles = new ArrayList<>(map.values());
        }

        return vehicles;
    }

    public boolean addStopPoint(String streetNum, String streetName, String suburb) {
        if (streetNum.equals("") || streetName.equals("") || suburb.equals("")) {
            return false;
        }
        return stopPointSet.add(new StopPoint(streetNum, streetName, suburb));
    }

    public boolean addRoute(String ID, String routeName, List<StopPoint> stopPoints) {
        if (routeName.equals("") || stopPoints.isEmpty()) {
            return false;
        } else {
            routeMap.putIfAbsent(ID, new ArrayList<>());
            routeMap.get(ID).add(new Route(routeName, stopPoints));

            return true;
        }
    }

    public List<Route> getUsersRoutes(String ID) {
        List<Route> routes = routeMap.get(ID);

        if (routes == null) {
            routes = new ArrayList<>();
        }

        return routes;
    }

    public void addTrip(String ID, List<StopPointEvent> stopPoints, TripDirection direction, LocalDate date, Vehicle vehicle, String routeName) {
        tripMap.putIfAbsent(ID, new ArrayList<>());
        tripMap.get(ID).add(new TripEvent(stopPoints, direction, date, vehicle, routeName, ID));
    }

    public List<TripEvent> getUsersTrips(String ID) {
        List<TripEvent> trips = tripMap.get(ID);

        if (trips == null) {
            trips = new ArrayList<>();
        }

        return trips;
    }

    public Set getStopPointSet() {
        return stopPointSet;
    }

    public Map<String, List<TripEvent>> getTripMap() {
        return tripMap;
    }

    public Map<String, License> getLicenseMap() {
        return licenseMap;
    }
}
