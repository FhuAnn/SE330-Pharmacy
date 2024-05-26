package com.example.se330_pharmacy.Models;

public class DetailBill {
    private int billId;
    private int productId;
    private float presentPrice;
    private int quantity;
    private String productName;

    public DetailBill(int billID, String productName, int quantities, String unit, float price) {
    }

    public DetailBill(int billId, int productId, float presentPrice, int quantity) {
        this.billId = billId;
        this.productId = productId;
        this.presentPrice = presentPrice;
        this.quantity = quantity;
    }

    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public float getPresentPrice() {
        return presentPrice;
    }

    public void setPresentPrice(float presentPrice) {
        this.presentPrice = presentPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
