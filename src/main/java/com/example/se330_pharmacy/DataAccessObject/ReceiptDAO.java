package com.example.se330_pharmacy.DataAccessObject;

import com.example.se330_pharmacy.Models.ConnectDB;
import com.example.se330_pharmacy.Models.Receipt;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReceiptDAO {
    public ObservableList<Receipt> GetReceiptsData()
    {
        ObservableList<Receipt> receipts = FXCollections.observableArrayList();
        String query = "SELECT rp.*, emp1.employname AS chargername, emp2.employname AS employname " +
                "FROM Receipt rp " +
                "JOIN Employee emp1 ON rp.personcharge_id = emp1.employee_id " +
                "JOIN Employee emp2 ON rp.employee_id = emp2.employee_id;";
        ConnectDB connectDB = new ConnectDB();
        try (ResultSet resultSet = connectDB.getData(query))
        {
            while (resultSet.next())
            {
                    Receipt receipt = new Receipt();
                    receipt.setReceipt_id(resultSet.getInt("receipt_id"));
                    receipt.setEmployee_id(resultSet.getInt("employee_id"));
                    receipt.setTenNhanVien(resultSet.getString("employname"));
                    receipt.setPersoncharge_id(resultSet.getInt("personcharge_id"));
                    receipt.setTenNguoiTra(resultSet.getString("chargername"));
                    receipt.setContent(resultSet.getString("content"));
                    receipt.setTotalPay(resultSet.getDouble("totalpay"));
                    receipt.setStatus(resultSet.getString("status"));
                    receipt.setNote(resultSet.getString("note"));
                    receipt.setCreateDate(resultSet.getDate("createdate"));
                    receipts.add(receipt);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return receipts;
    }

    public ObservableList<Receipt> GetReceiptsToday(Date date)
    {
        ObservableList<Receipt> receipts = FXCollections.observableArrayList();
        ConnectDB connect = new ConnectDB();
        String query = "SELECT rp.*, emp1.employname AS chargername, emp2.employname AS employname FROM receipt rp " +
                "JOIN employee emp1 ON rp.personcharge_id = emp1.employee_id " +
                "JOIN employee emp2 ON rp.employee_id = emp2.employee_id " +
                "WHERE rp.createdate= ? ";
        try (PreparedStatement statement = connect.getConnection().prepareStatement(query))
        {
            statement.setDate(1,date);
            try (ResultSet resultSet = statement.executeQuery())
            {
                while(resultSet.next())
                {
                    Receipt receipt = new Receipt();
                    receipt.setReceipt_id(resultSet.getInt("receipt_id"));
                    receipt.setEmployee_id(resultSet.getInt("employee_id"));
                    receipt.setTenNhanVien(resultSet.getString("employname"));
                    receipt.setPersoncharge_id(resultSet.getInt("personcharge_id"));
                    receipt.setTenNguoiTra(resultSet.getString("chargername"));
                    receipt.setContent(resultSet.getString("content"));
                    receipt.setTotalPay(resultSet.getDouble("totalpay"));
                    receipt.setStatus(resultSet.getString("status"));
                    receipt.setNote(resultSet.getString("note"));
                    receipt.setCreateDate(resultSet.getDate("createdate"));
                    receipts.add(receipt);
                }

            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return receipts;
    }

    public int AddReceiptToDB(Receipt receipt)
    {
        String query ="INSERT INTO receipt (content,createdate,totalpay,note,status,personcharge_id,payslip_id,employee_id) VALUES(?,?,?,?,?,?,?,?) RETURNING receipt_id";
        ConnectDB connectDB = new ConnectDB();
        int receipt_id = -1;
        try (PreparedStatement statement = connectDB.getConnection().prepareStatement(query)) {
            statement.setString(1,receipt.getContent());
            statement.setDate(2,receipt.getCreateDate());
            statement.setDouble(3,receipt.getTotalPay());
            statement.setString(4,receipt.getNote());
            statement.setString(5,receipt.getStatus());
            statement.setInt(6,receipt.getPersoncharge_id());
            statement.setInt(7,receipt.getPayslip_id());
            statement.setInt(8,receipt.getEmployee_id());
            ResultSet resultSet = statement.executeQuery(); // Sử dụng executeQuery() thay vì executeUpdate()
            if (resultSet.next()) {
                receipt_id = resultSet.getInt(1); // Lấy giá trị của makhambenh từ ResultSet
            }
        }
        catch (SQLException e ) {
            e.printStackTrace();
        }
        return receipt_id;
    }

}
