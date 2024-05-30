package com.example.se330_pharmacy.Models;

import java.util.Date;

public class Import {
    private int importId;
    private int employeeId;
    private int supplierId;
    private float totalPrice;
    private int productId;
    private float importPrice;
    private int quantity;
    private float total;
    private Date formDate;

    // Constructors
    public Import() {}

    public Import(int importId, int employeeId, int supplierId, float totalPrice, int productId,
                       float importPrice, int quantity, float total, Date formDate) {
        this.importId = importId;
        this.employeeId = employeeId;
        this.supplierId = supplierId;
        this.totalPrice = totalPrice;
        this.productId = productId;
        this.importPrice = importPrice;
        this.quantity = quantity;
        this.total = total;
        this.formDate = formDate;
    }

    // Getters and Setters
    public int getImportId() {
        return importId;
    }

    public void setImportId(int importId) {
        this.importId = importId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public float getImportPrice() {
        return importPrice;
    }

    public void setImportPrice(float importPrice) {
        this.importPrice = importPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public Date getFormDate() {
        return formDate;
    }

    public void setFormDate(Date formDate) {
        this.formDate = formDate;
    }
}

