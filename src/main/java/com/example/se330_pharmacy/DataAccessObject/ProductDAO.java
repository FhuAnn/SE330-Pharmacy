package com.example.se330_pharmacy.DataAccessObject;

import com.example.se330_pharmacy.Models.ConnectDB;
import com.example.se330_pharmacy.Models.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductDAO {
    private final ConnectDB connectDB = ConnectDB.getInstance();
    public ProductDAO() {
    }

    public ObservableList<Product> getAllProduct() {
        ObservableList<Product> products = FXCollections.observableArrayList();
        String query = "SELECT product_id , productname, price_import, product.small_unit , unit.small_unit, " +
                "product.big_unit, unit.big_unit, unit.value FROM product,unit WHERE product.unit_id = unit.unit_id ";

        try (PreparedStatement preparedStatement = connectDB.databaseLink.prepareStatement(query);
            ResultSet rs = preparedStatement.executeQuery()) {

            while (rs.next()) {
                Product product = new Product();
                product.setProductId(rs.getInt(1));
                product.setProductName(rs.getString(2));
                product.setProductImportPrice(rs.getInt(3));
                product.setProductSmallUnit(rs.getString(5));
                product.setProductSmallUnitQuantities(rs.getInt(4));
                product.setProductBigUnit(rs.getString(7));
                product.setProductBigUnitQuantities(rs.getInt(6));
                product.setProductCoef(rs.getInt(8));
                products.add(product);
            }
        } catch (Exception e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
        return products;
    }


    public ObservableList<Product> getAllProductExport() {
        ObservableList<Product> products = FXCollections.observableArrayList();
        String query = "SELECT product.product_id , productname, price_import, product.description ,product.origin,  unit.big_unit ,producttype.typename FROM product,unit,producttype " +
                "WHERE product.unit_id = unit.unit_id AND product.producttype_id = producttype.producttype_id ORDER BY product.product_id asc";

        try (PreparedStatement preparedStatement = connectDB.databaseLink.prepareStatement(query);
             ResultSet rs = preparedStatement.executeQuery()) {

            while (rs.next()) {
                Product product = new Product();
                product.setProductId(rs.getInt(1));
                product.setProductName(rs.getString(2));
                product.setProductImportPrice(rs.getInt(3));
                product.setProductDescription(rs.getString(4));
                product.setProductOrigin(rs.getString(5));
                product.setProductBigUnit(rs.getString(6));
                product.setProductType(rs.getString(7));
                products.add(product);
            }
        } catch (Exception e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
        return products;
    }

    public ObservableList<Product> getAllProductBigQuantity() {
        ObservableList<Product> products = FXCollections.observableArrayList();
        String query = "Select product_id ,product.big_unit from product order by product_id asc";

        try (PreparedStatement preparedStatement = connectDB.databaseLink.prepareStatement(query);
             ResultSet rs = preparedStatement.executeQuery()) {

            while (rs.next()) {
                Product product = new Product();
                product.setProductId(rs.getInt(1));
                product.setProductBigUnitQuantities(rs.getInt(2));
                products.add(product);
            }
        } catch (Exception e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
        return products;
    }
}
