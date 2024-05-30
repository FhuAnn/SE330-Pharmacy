package com.example.se330_pharmacy.DataAccessObject;

import com.example.se330_pharmacy.Models.ConnectDB;
import com.example.se330_pharmacy.Models.Report;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReportDAO {
    ConnectDB connectDB = new ConnectDB();

    public List<Report> getAllReports() {
        List<Report> reports = new ArrayList<>();
        String query = "SELECT id, medicineName, purchasePrice, quantityPurchased, sellingPrice, quantitySold, remainingRate, unit FROM report";

        try (Connection connection = connectDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Report report = new Report(
                        resultSet.getInt("id"),
                        resultSet.getString("medicineName"),
                        resultSet.getDouble("purchasePrice"),
                        resultSet.getInt("quantityPurchased"),
                        resultSet.getDouble("sellingPrice"),
                        resultSet.getInt("quantitySold"),
                        resultSet.getDouble("remainingRate"),
                        resultSet.getString("unit")
                );
                reports.add(report);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reports;
    }

    public List<Report> searchReports(String searchText) {
        List<Report> reports = new ArrayList<>();
        String query = "SELECT id, medicineName, purchasePrice, quantityPurchased, sellingPrice, quantitySold, remainingRate, unit " +
                "FROM report WHERE LOWER(medicineName) LIKE ? OR CAST(id AS TEXT) LIKE ?";

        try (Connection connection = connectDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            String searchPattern = "%" + searchText.toLowerCase() + "%";
            statement.setString(1, searchPattern);
            statement.setString(2, searchPattern);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Report report = new Report(
                            resultSet.getInt("id"),
                            resultSet.getString("medicineName"),
                            resultSet.getDouble("purchasePrice"),
                            resultSet.getInt("quantityPurchased"),
                            resultSet.getDouble("sellingPrice"),
                            resultSet.getInt("quantitySold"),
                            resultSet.getDouble("remainingRate"),
                            resultSet.getString("unit")
                    );
                    reports.add(report);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reports;
    }
}
