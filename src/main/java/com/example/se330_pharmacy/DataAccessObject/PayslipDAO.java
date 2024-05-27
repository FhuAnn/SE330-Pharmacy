package com.example.se330_pharmacy.DataAccessObject;

import com.example.se330_pharmacy.Models.ConnectDB;
import com.example.se330_pharmacy.Models.Employee;
import com.example.se330_pharmacy.Models.Payslip;
import com.example.se330_pharmacy.Models.Receipt;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PayslipDAO {
    ConnectDB connectDB = ConnectDB.getInstance();

    public ObservableList<Payslip> GetPaySlipData()
    {
        ObservableList<Payslip> payslips = FXCollections.observableArrayList();
        String query = "SELECT * FROM payslip pl, Employee emp  WHERE pl.employee_id = emp.employee_id";
        try (ResultSet resultSet = connectDB.getData(query))
        {
            while (resultSet.next())
            {
                Payslip payslip = new Payslip();
                payslip.setPayslip_id(resultSet.getInt("payslip_id"));
                payslip.setReceipt_id(resultSet.getInt("receipt_id"));
                payslip.setEmployee_id(resultSet.getInt("employee_id"));
                payslip.setTenNhanVien(resultSet.getString("employname"));
                payslip.setViTriLamViec(resultSet.getString("position"));
                payslip.setCreateDate(resultSet.getDate("createdate"));
                payslip.setContent(resultSet.getString("content"));
                payslip.setTotalPay(resultSet.getDouble("totalpay"));
                payslip.setStatus(resultSet.getString("status"));
                payslip.setNote(resultSet.getString("note"));
                payslip.setReceipt_id(resultSet.getInt("receipt_id"));
                payslips.add(payslip);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return payslips;
    }
    public boolean UpdatePayslipCompleted(int _id)
    {
        String query = "UPDATE payslip SET status = 'Completed' WHERE payslip_id = ?";
        try(PreparedStatement statement = connectDB.databaseLink.prepareStatement(query)) {
            statement.setInt(1,_id);
            int affRow = statement.executeUpdate();
            if(affRow ==0 ) return false;
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return true;
    }
    public boolean AddPaySlip(Payslip payslip)
    {
        String query = "INSERT INTO payslip (employee_id,content,createdate,totalpay,note,status)" +
                " VALUES (?,?,?,?,?,?)";
        try(PreparedStatement statement = connectDB.databaseLink.prepareStatement(query)) {
            statement.setInt(1,payslip.getEmployee_id());
            statement.setString(2,payslip.getContent());
            statement.setDate(3,payslip.getCreateDate());
            statement.setDouble(4,payslip.getTotalPay());
            statement.setString(5,payslip.getNote());
            statement.setString(6,payslip.getStatus());
            int affRow = statement.executeUpdate();
            if(affRow ==0 ) return false;
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return true;
    }
    public boolean UpdatePaySlip(Payslip payslip)
    {
        String query = "UPDATE payslip SET employee_id = ? , content = ?, createdate = ?,totalpay = ?,note =? ,status = ? WHERE payslip_id = ?";
        try(PreparedStatement statement = connectDB.databaseLink.prepareStatement(query)) {
            statement.setInt(1,payslip.getEmployee_id());
            statement.setString(2,payslip.getContent());
            statement.setDate(3,payslip.getCreateDate());
            statement.setDouble(4,payslip.getTotalPay());
            statement.setString(5,payslip.getNote());
            statement.setString(6,payslip.getStatus());
            statement.setInt(7,payslip.getPayslip_id());
            int affRow = statement.executeUpdate();
            if(affRow ==0 ) return false;
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return true;
    }
}
