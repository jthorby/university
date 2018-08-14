package model;

import java.util.List;

public class Route {
    private String name;
    private List<StopPoint> stopPoints;

    public Route(String name, List<StopPoint> stopPoints) {
        this.name = name;
        this.stopPoints = stopPoints;
    }

    public List<StopPoint> getStopPoints() {
        return stopPoints;
    }

    public String getName() {
        return name;
    }

    @Override public String toString() {
        String result = name + ": ";

        for (StopPoint sp : stopPoints) {
            result += sp.toString() + " ";
        }

        return result;
    }
}
