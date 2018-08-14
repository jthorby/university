package model;

import java.time.LocalDate;

public class Vehicle {

    private String type;
    private String model;
    private String colour;
    private String plate;
    private int year;
    private int seatsNum;
    private LocalDate wofExpiry;
    private LocalDate regoExpiry;

    public Vehicle(String type, String model, String colour, String plate, int year, int seatsNum, LocalDate wofExpiry, LocalDate regoExpiry) {
        this.type = type;
        this.model = model;
        this.colour = colour;
        this.plate = plate;
        this.year = year;
        this.seatsNum = seatsNum;
        this.wofExpiry = wofExpiry;
        this.regoExpiry = regoExpiry;
    }

    public int getSeatsNum() {
        return seatsNum;
    }

    public String getType() {
        return type;
    }

    public String getModel() {
        return model;
    }

    public String getColour() {
        return colour;
    }

    public String getPlate() {
        return plate;
    }

    public int getYear() {
        return year;
    }

    public LocalDate getWofExpiry() {
        return wofExpiry;
    }

    public void setWofExpiry(LocalDate wofExpiry) {
        this.wofExpiry = wofExpiry;
    }

    public LocalDate getRegoExpiry() {
        return regoExpiry;
    }

    public void setRegoExpiry(LocalDate regoExpiry) {
        this.regoExpiry = regoExpiry;
    }

    @Override public String toString() {
        return String.format("%d %s", year, model);
    }
}
