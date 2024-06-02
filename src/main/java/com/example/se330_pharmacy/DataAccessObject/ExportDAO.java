package com.example.se330_pharmacy.DataAccessObject;

import com.example.se330_pharmacy.Models.ConnectDB;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ExportDAO {
    final private ConnectDB connectDB = ConnectDB.getInstance();

    public ExportDAO() {

    }

    public String addData(String employee, String reason, String totalPrice) {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        // Lấy ngày hiện tại
        LocalDate currentDate = LocalDate.now();
        // Định dạng ngày hiện tại theo định dạng "dd-MM-yyyy"
        String formattedDate = currentDate.format(dateFormat);
        String sql = "INSERT INTO ExportForm (Employee_id, ExportReason, ExportDate, TotalMoney) " +
                "VALUES (?, ?, ?, ?) RETURNING exportform_id";

        try (PreparedStatement stmt = connectDB.databaseLink.prepareStatement(sql)) {

            stmt.setInt(1, Integer.parseInt(employee));
            stmt.setString(2, reason);
            stmt.setString(3, formattedDate);
            stmt.setInt(4, Integer.parseInt(totalPrice));

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return String.valueOf(rs.getInt(1));
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void addDetailData(int productID, String exportID, int quantity, double price, double totalPrice) {
        String sql = "INSERT INTO DetailExportForm (Product_id, Exportform_id, Price, Quantity,  total) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connectDB.databaseLink.prepareStatement(sql)) {

            stmt.setInt(1, productID);
            stmt.setInt(2, Integer.parseInt(exportID));
            stmt.setDouble(3, price);
            stmt.setInt(4, quantity);
            stmt.setDouble(5, totalPrice);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateProduct(int quantity, int productID) {
        String sql = "UPDATE Product SET big_unit = big_unit - ? WHERE Product_id = ?";

        try (PreparedStatement stmt = connectDB.databaseLink.prepareStatement(sql)) {

            stmt.setInt(1, quantity);
            stmt.setInt(2, productID);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
