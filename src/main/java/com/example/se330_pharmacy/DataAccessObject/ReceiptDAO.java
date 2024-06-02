package com.example.se330_pharmacy.DataAccessObject;

import com.example.se330_pharmacy.Models.ConnectDB;
import com.example.se330_pharmacy.Models.Receipt;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class ReceiptDAO {
    ConnectDB connectDB = ConnectDB.getInstance();

    public ObservableList<Receipt> GetReceiptsData()
    {
        ObservableList<Receipt> receipts = FXCollections.observableArrayList();
        String query = "SELECT rp.*, emp1.employname AS chargername, emp2.employname AS employname,emp1.position as pos_charger ,emp2.position as pos_emp " +
                "FROM Receipt rp " +
                "JOIN Employee emp1 ON rp.personcharge_id = emp1.employee_id " +
                "JOIN Employee emp2 ON rp.employee_id = emp2.employee_id " +
                "JOIN Payslip pl ON pl.receipt_id = rp.receipt_id";
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
                    receipt.setVitriNguoiTra(resultSet.getString("pos_charger"));
                    receipt.setViTriNhanVien(resultSet.getString("pos_emp"));
                    receipts.add(receipt);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return receipts;
    }


    public int AddReceiptToDB(Receipt receipt)
    {
        String query ="INSERT INTO receipt (content,createdate,totalpay,note,status,personcharge_id,payslip_id,employee_id) VALUES(?,?,?,?,?,?,?,?) RETURNING receipt_id";
        int receipt_id = -1;
        try (PreparedStatement statement = connectDB.databaseLink.prepareStatement(query)) {
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
    public boolean UpdateReceiptToDB(Receipt receipt)
    {
        String query ="UPDATE receipt SET createdate = ?, status= ?, note = ? WHERE receipt_id = ?  ";
        int receipt_id = -1;
        try (PreparedStatement statement = connectDB.databaseLink.prepareStatement(query)) {
            statement.setDate(1, Date.valueOf(LocalDate.now()));
            statement.setString(2,receipt.getStatus());
            statement.setString(3,receipt.getNote());
            statement.setInt(4,receipt.getReceipt_id());
            int aff = statement.executeUpdate(); // Sử dụng executeQuery() thay vì executeUpdate()
            if(aff > 0) return true;
        }
        catch (SQLException e ) {
            e.printStackTrace();
        }
        return false;
    }

}
