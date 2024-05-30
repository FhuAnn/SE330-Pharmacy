package com.example.se330_pharmacy.Models;

public class Report {
    private int medicineId;
    private String medicineName;
    private double purchasePrice;
    private int quantityPurchased;
    private double sellingPrice;
    private int quantitySold;
    private double remainingRate;
    private String unit;

    public Report(int id, String medicineName, double purchasePrice, int quantityPurchased,
                  double sellingPrice, int quantitySold, double remainingRate, String unit) {
        this.medicineId = id;
        this.medicineName = medicineName;
        this.purchasePrice = purchasePrice;
        this.quantityPurchased = quantityPurchased;
        this.sellingPrice = sellingPrice;
        this.quantitySold = quantitySold;
        this.remainingRate = remainingRate;
        this.unit = unit;
    }
    public int getId() {
        return medicineId;
    }

    public void setId(int id) {
        this.medicineId = medicineId;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public int getQuantityPurchased() {
        return quantityPurchased;
    }

    public void setQuantityPurchased(int quantityPurchased) {
        this.quantityPurchased = quantityPurchased;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public int getQuantitySold() {
        return quantitySold;
    }

    public void setQuantitySold(int quantitySold) {
        this.quantitySold = quantitySold;
    }

    public double getRemainingRate() {
        return remainingRate;
    }

    public void setRemainingRate(double remainingRate) {
        this.remainingRate = remainingRate;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
