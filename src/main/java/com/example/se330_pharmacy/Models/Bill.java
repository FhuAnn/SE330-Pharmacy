package com.example.se330_pharmacy.Models;

public class Bill {
    private int billId;
    private int employeeId;
    private String customerName;
    private String phoneNumber;
    private float billValue;
    private int productId;
    private String productName;
    private float price;
    private int quantities;
    private String unitName;

    public Bill(int billId, String customerName, String phoneNumber, float billValue, String dateBill) {
    }

    public Bill(int billId, int employeeId, String customerName, String phoneNumber, float billValue, int productId, String productName, float price, int quantities, String unitName) {
        this.billId = billId;
        this.employeeId = employeeId;
        this.customerName = customerName;
        this.phoneNumber = phoneNumber;
        this.billValue = billValue;
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantities = quantities;
        this.unitName = unitName;
    }

    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public float getBillValue() {
        return billValue;
    }

    public void setBillValue(float billValue) {
        this.billValue = billValue;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getQuantities() {
        return quantities;
    }

    public void setQuantities(int quantities) {
        this.quantities = quantities;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }
}
