package com.example.se330_pharmacy.Models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DetailImport {
    private int importId;
    private String productId;
    private float importPrice;
    private String productName;
    private int quantity;
    private double total;
    private String supplier;

    public DetailImport(String productid, String productname, float v, int i, double v1, String sup) {
        this.productId = productid;
        this.productName = productname;
        this.importPrice = v;
        this.quantity = i;
        this.total = v1;
        this.supplier = sup;
    }

    public DetailImport(int importId, String text, String text1, float v, int i, double v1, Object value) {
        this.importId = importId;
        this.productId = text;
        this.productName = text1;
        this.importPrice = v;
        this.quantity = i;
        this.total = v1;
        this.supplier = (String) value;
    }

    public static String getProduct_id() {
        return productId;
    }

    public void setProduct_id(String product_id) {
        this.productId = product_id;
    }

    // Getter và Setter cho importPrice
    public static float getImportPrice() {
        return importPrice;
    }

    public void setImportPrice(float importPrice) {
        this.importPrice = importPrice;
    }

    // Getter và Setter cho quantity
    public static int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Getter và Setter cho total
    public static double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public DetailImport(String productID, String productName, float productPrice, int productQuantities, double productTotal) {
        this.productId = productID;
        this.productName = productName;
        this.importPrice = productPrice;
        this.quantity = productQuantities;
        this.total = productTotal;
    }

    public boolean addDetailData(String proId, String id, float importPrice, int quantity, double total) {
        String query = "INSERT INTO DetailImportForm (Product_id, ImportForm_id, ImportPrice, Quantity, Total) VALUES (?, ?, ?, ?, ?)";

        try {
            Connection connection = ConnectDB.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, Integer.parseInt(proId));
            preparedStatement.setInt(2, Integer.parseInt(id));
            preparedStatement.setFloat(3, importPrice);
            preparedStatement.setInt(4, quantity);
            preparedStatement.setDouble(5, total);

            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public int getImportId() {
        return importId;
    }

    public void setImportId(int importId) {
        this.importId = importId;
    }
}

