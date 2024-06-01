package com.example.se330_pharmacy.Models;

public class ExportItem {
    private int productId;
    private String productName;
    private long productPrice;
    private int productQuantities;
    private long totalExportValue;

    public ExportItem() {
    }

    public ExportItem(int productId, String productName, long productPrice, int productQuantities, long totalExportValue) {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productQuantities = productQuantities;
        this.totalExportValue = totalExportValue;
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

    public long getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(long productPrice) {
        this.productPrice = productPrice;
    }

    public int getProductQuantities() {
        return productQuantities;
    }

    public void setProductQuantities(int productQuantities) {
        this.productQuantities = productQuantities;
    }

    public long getTotalExportValue() {
        return totalExportValue;
    }

    public void setTotalExportValue(long totalExportValue) {
        this.totalExportValue = totalExportValue;
    }
}
