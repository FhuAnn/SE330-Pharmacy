package com.example.se330_pharmacy.DataAccessObject;

import com.example.se330_pharmacy.Models.ConnectDB;
import com.example.se330_pharmacy.Models.Supplier;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SupplierDAO {
    ConnectDB connectDB =ConnectDB.getInstance();
    public ObservableList<Supplier> getSuppliers() {
        String sqlQuery = "SELECT partner_id, partnername,address, phonenumber,email FROM partner";
        ObservableList<Supplier> suppliers = FXCollections.observableArrayList();
        try (ResultSet rs = connectDB.getPreparedStatement(sqlQuery).executeQuery()) {
            while(rs.next()) {
                Supplier suppier = new Supplier(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5));
                suppliers.add(suppier);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return suppliers;
    }
}
