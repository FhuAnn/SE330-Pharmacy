package com.example.se330_pharmacy.Models;

public class Unit {
    private String bigUnit;
    private String smallUnit;

    public Unit() {
    }

    public Unit(String bigUnit, String smallUnit) {
        this.bigUnit = bigUnit;
        this.smallUnit = smallUnit;
    }

    public String getBigUnit() {
        return bigUnit;
    }

    public void setBigUnit(String bigUnit) {
        this.bigUnit = bigUnit;
    }

    public String getSmallUnit() {
        return smallUnit;
    }

    public void setSmallUnit(String smallUnit) {
        this.smallUnit = smallUnit;
    }
}
