package com.example.se330_pharmacy.DataAccessObject;

import com.example.se330_pharmacy.Models.Bill;
import com.example.se330_pharmacy.Models.ConnectDB;
import com.example.se330_pharmacy.Models.Unit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BillDAO {
    private ConnectDB connectDB = ConnectDB.getInstance();

    public BillDAO() {
    }

    public String addBill(String employeeId, String cusName,String phoneNumber,String billValue) {
        Date now = Date.valueOf(LocalDate.now());

        String generatedId = null;
        String sql = "INSERT INTO Bill (Employee_id, Cus_Name, PhoneNumber, BillValue, DateBill) VALUES (?, ?, ?, ?, ?)";
        try (
             PreparedStatement pstmt = connectDB.databaseLink.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, Integer.parseInt(employeeId));
            pstmt.setString(2, cusName);
            pstmt.setString(3, phoneNumber);
            pstmt.setDouble(4, Double.parseDouble(billValue));
            pstmt.setDate(5, now);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        generatedId = String.valueOf(rs.getInt(1));
                    }
                } catch (SQLException ex) {
                    System.out.println("Error retrieving generated key: " + ex.getMessage());
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error executing insert statement: " + ex.getMessage());
        }

        return generatedId;


    }

    public boolean autoCreateReceipts(String employeeId, String contentReceipts, String billValue, String status, String note) {
        java.util.Date date = new java.util.Date();
        java.sql.Date currentDate = new java.sql.Date(date.getTime());

        String sql = "INSERT INTO Receipt (Employee_id, Content, CreateDate, TotalPay, Status, Note) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connectDB.databaseLink.prepareStatement(sql)) {
            preparedStatement.setInt(1, Integer.parseInt(employeeId));
            preparedStatement.setString(2, contentReceipts);
            preparedStatement.setDate(3, currentDate);
            preparedStatement.setDouble(4, Double.parseDouble(billValue));
            preparedStatement.setString(5, status);
            preparedStatement.setString(6, note);

            int rowsAffected = preparedStatement.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public List<Unit> getUnit(String id) {
        List<Unit> units = new ArrayList<>();

        String sqlQuery = STR."SELECT pro.big_unit, pro.small_unit  FROM Product pro, Unit uni WHERE pro.Product_id = '\{id}' AND pro.Unit_id = uni.Unit_id";

        try (Statement statement = connectDB.databaseLink.createStatement();
             ResultSet resultSet = statement.executeQuery(sqlQuery)) {

            while (resultSet.next()) {
                String bigUnit = resultSet.getString("big_unit");
                String smallUnit = resultSet.getString("small_unit");
                units.add(new Unit(bigUnit, smallUnit));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return units;
    }

    public int getCoef(int id) throws SQLException {
        String sqlQuery = "SELECT uni.Value  FROM Product pro, Unit uni WHERE uni.Unit_id = pro.Unit_id AND pro.Product_id = ?";
        PreparedStatement stmt = connectDB.getPreparedStatement(sqlQuery);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getInt("Value");
        }
        return -1;
    }

    public int getBigQuan(int id) throws SQLException {
        String sqlQuery = "SELECT big_unit FROM Product WHERE Product_id = ?";
        PreparedStatement stmt = connectDB.getPreparedStatement(sqlQuery);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getInt("big_unit");
        }
        return -1;
    }

    public int getSmallQuan(int id) throws SQLException {
        String sqlQuery = "SELECT small_unit FROM Product WHERE Product_id = ?";
        PreparedStatement stmt = connectDB.getPreparedStatement(sqlQuery);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getInt("small_unit");
        }
        return -1;
    }

    public boolean updateProduct(String id, String bigQuantity, String smallQuantity) {

        String query;
        // Update lv1 and lv2
        query = "UPDATE Product SET big_unit = ?, small_unit = ? WHERE Product_id = ?";
        try (PreparedStatement preparedStatement = connectDB.databaseLink.prepareStatement(query)) {
            preparedStatement.setInt(1, Integer.parseInt(smallQuantity));
            preparedStatement.setInt(2, Integer.parseInt(bigQuantity));
            preparedStatement.setInt(3, Integer.parseInt(id));

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    public ObservableList<Bill> getBillData() {
        ObservableList<Bill> billList = FXCollections.observableArrayList();
        String sqlQuery = "SELECT Bill_id, Cus_Name AS Customer, PhoneNumber AS Phone, BillValue AS Value, DateBill AS Date FROM Bill";

        try (Statement stmt = connectDB.databaseLink.createStatement(); ResultSet rs = stmt.executeQuery(sqlQuery)) {
            while (rs.next()) {
                int billId = rs.getInt("Bill_id");
                String customerName = rs.getString("Customer");
                String phoneNumber = rs.getString("Phone");
                int billValue = rs.getInt("Value");
                Date dateBill = rs.getDate("Date");

                Bill bill = new Bill(billId, customerName, phoneNumber, billValue, dateBill);
                billList.add(bill);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return billList;
    }
   /* public ObservableList<Product> getProductData() throws SQLException {
        ObservableList<Product> data = FXCollections.observableArrayList();
        String sqlQuery = "SELECT Product_id AS ID, ProductName AS Name, Price FROM Product";
        ResultSet rs = connect.getData(sqlQuery);

        while (rs.next()) {
            int id = rs.getInt("ID");
            String name = rs.getString("Name");
            double price = rs.getDouble("Price");

            data.add(new Product(id, name, price));
        }
        return data;
    }

    public ResultSet getBillData() {
        String sqlQuery = "SELECT Bill_id, Cus_Name AS Customer, PhoneNumber AS Phone, BillValue AS Value, DateBill AS Date FROM Bill";
        return connectDB.getData(sqlQuery);
    }

    public ResultSet getDetailBill(String bill_id) throws SQLException {
        String sqlQuery = "SELECT Bill_id AS BillID, pro.ProductName AS 'Product Name', Quantities, Unit_Name AS Unit, pro.Price "
                + "FROM DetailBill dtl, Product pro "
                + "WHERE Bill_id = ? AND pro.Product_id = dtl.Product_id";
        PreparedStatement stmt = connectDB.getPreparedStatement(sqlQuery);
        stmt.setString(1, bill_id);
        return stmt.executeQuery();
    }

    public ResultSet searchData(String search) throws SQLException {
        String sqlQuery = "SELECT pro.Product_id AS ID, pro.ProductName AS Name, pro.Price AS 'Price-UnitBig', pro.lv2Quantity AS 'Available-LV2', "
                + "uni.Unit_Namelv2 AS 'Unit(Small)', pro.lv1Quantity AS 'Available-LV1', uni.Unit_Namelv1 AS 'Unit(Big)', uni.Value AS 'Coef' "
                + "FROM Product pro, Unit uni "
                + "WHERE (pro.Product_id LIKE ? OR pro.ProductName LIKE ?) AND uni.Unit_id = pro.Unit_id";
        PreparedStatement stmt = connectDB.getPreparedStatement(sqlQuery);
        stmt.setString(1, search + "%");
        stmt.setString(2, search + "%");
        return stmt.executeQuery();
    }

    public ResultSet searchBill(String search) throws SQLException {
        String sqlQuery = "SELECT Bill_id, Cus_Name AS Customer, PhoneNumber AS Phone, BillValue AS Value, DateBill AS Date "
                + "FROM Bill WHERE (Bill_id LIKE ? OR Cus_Name LIKE ? OR PhoneNumber LIKE ?)";
        PreparedStatement stmt = connectDB.getPreparedStatement(sqlQuery);
        stmt.setString(1, search + "%");
        stmt.setString(2, search + "%");
        stmt.setString(3, search + "%");
        return stmt.executeQuery();
    }

    public String addData(String employee, String name, String phone, String billValue) throws SQLException {
        String sql = "INSERT INTO Bill (Employee_id, Cus_Name, PhoneNumber, BillValue, DateBill) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement stmt = connectDB.getPreparedStatement(sql);
        stmt.setInt(1, Integer.parseInt(employee));
        stmt.setString(2, name);
        stmt.setString(3, phone);
        stmt.setDouble(4, Double.parseDouble(billValue));
        stmt.setDate(5,  new java.sql.Date(System.currentTimeMillis()));
        int affectedRows = stmt.executeUpdate();

        if (affectedRows > 0) {
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                return String.valueOf(generatedKeys.getInt(1));
            }
        }
        return null;
    }

    public String getCoef(String id) throws SQLException {
        String sqlQuery = "SELECT uni.Value AS 'Value' FROM Product pro, Unit uni WHERE uni.Unit_id = pro.Unit_id AND pro.Product_id = ?";
        PreparedStatement stmt = connectDB.getPreparedStatement(sqlQuery);
        stmt.setString(1, id);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getString("Value");
        }
        return null;
    }

    public String getQuanLv1(String id) throws SQLException {
        String sqlQuery = "SELECT lv1Quantity FROM Product WHERE Product_id = ?";
        PreparedStatement stmt = connectDB.getPreparedStatement(sqlQuery);
        stmt.setString(1, id);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getString("lv1Quantity");
        }
        return null;
    }

    public String getQuanLv2(String id) throws SQLException {
        String sqlQuery = "SELECT lv2Quantity FROM Product WHERE Product_id = ?";
        PreparedStatement stmt = connectDB.getPreparedStatement(sqlQuery);
        stmt.setString(1, id);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getString("lv2Quantity");
        }
        return null;
    }

    public ResultSet getUnit(String id) throws SQLException {
        String sqlQuery = "SELECT Unit_Namelv1, Unit_Namelv2 FROM Product pro, Unit uni WHERE pro.Product_id = ? AND pro.Unit_id = uni.Unit_id";
        PreparedStatement stmt = connectDB.getPreparedStatement(sqlQuery);
        stmt.setString(1, id);
        return stmt.executeQuery();
    }

    public boolean updateProduct(String id, String quanLv1, String quanLv2) throws SQLException {
        String sql = "UPDATE Product SET lv2Quantity = ?, lv1Quantity = ? WHERE Product_id = ?";
        PreparedStatement stmt = connectDB.getPreparedStatement(sql);
        stmt.setInt(1, Integer.parseInt(quanLv2));
        stmt.setInt(2, Integer.parseInt(quanLv1));
        stmt.setInt(3, Integer.parseInt(id));
        int affectedRows = stmt.executeUpdate();
        return affectedRows > 0;
    }

    public boolean autoCreateReceipts(String employeeId, String content, String totalPay, String status, String note) throws SQLException {
        String sql = "INSERT INTO Receipt (Employee_id, Content, CreateDate, TotalPay, Status, Note) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = connectDB.getPreparedStatement(sql);
        stmt.setInt(1, Integer.parseInt(employeeId));
        stmt.setString(2, content);
        stmt.setDate(3,  new java.sql.Date(System.currentTimeMillis()));
        stmt.setDouble(4, Double.parseDouble(totalPay));
        stmt.setString(5, status);
        stmt.setString(6, note);
        int affectedRows = stmt.executeUpdate();
        return affectedRows > 0;
    }

    public ResultSet getProductOffer() throws SQLException {
        String sqlQuery = "SELECT Product_id AS ID, ProductName AS Name, Price, Description, Origin, Unit.Unit_Namelv1 AS Unit, TypeName AS Type "
                + "FROM Product, ProductType, Unit "
                + "WHERE Product.ProductType = ProductType.ProductType_id AND Unit.Unit_id = Product.Unit_id AND lv1Quantity < 5";
        return connectDB.getData(sqlQuery);
    }*/


}
