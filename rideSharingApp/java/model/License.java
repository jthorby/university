package model;

import java.time.LocalDate;

public class License {

    private String type;
    private String licenseNumber;
    private LocalDate issueDate;
    private LocalDate expiryDate;

    public License(String type, String licenseNumber, LocalDate issueDate, LocalDate expiryDate) {
        this.type = type;
        this.licenseNumber = licenseNumber;
        this.issueDate = issueDate;
        this.expiryDate = expiryDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }
}
