package com.example.se330_pharmacy.DataAccessObject;

import com.example.se330_pharmacy.Models.ConnectDB;
import com.example.se330_pharmacy.Models.DetailImport;
import com.example.se330_pharmacy.Models.Import;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ImportDAO {
    private static final ConnectDB connectDB = ConnectDB.getInstance();

    public ImportDAO() {}

    public static List<String> getSuppliers() {
        String sqlQuery = "SELECT SupplierName FROM Supplier";
        List<String> suppliers = new ArrayList<>();

        try (ResultSet rs = connectDB.getData(sqlQuery)) {
            while (rs.next()) {
                suppliers.add(rs.getString("SupplierName"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return suppliers;
    }

    public ResultSet getProductData() {
        String sqlQuery = "SELECT Product_id AS ID, ProductName AS Name, Price, Description, Origin, " +
                "Unit.Unit_Namelv1 AS Unit, TypeName AS Type " +
                "FROM Product, ProductType, Unit " +
                "WHERE Product.ProductType = ProductType.ProductType_id " +
                "AND Unit.Unit_id = Product.Unit_id";
        return connectDB.getData(sqlQuery);
    }

    public ResultSet searchProductData(String search) {
        String sqlQuery = "SELECT Product_id AS ID, ProductName AS Name, Price, Description, Origin, " +
                "Unit.Unit_Namelv1, TypeName AS Type " +
                "FROM Product, ProductType, Unit " +
                "WHERE Product.ProductType = ProductType.ProductType_id " +
                "AND Unit.Unit_id = Product.Unit_id " +
                "AND (Product_id LIKE ? OR ProductName LIKE ?)";

        try {
            PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement(sqlQuery);
            preparedStatement.setString(1, search + "%");
            preparedStatement.setString(2, "%" + search + "%");
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getSupplierIdByName(String name) {
        String sqlQuery = "SELECT Supplier_id FROM Supplier WHERE SupplierName = ?";

        try {
            PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement(sqlQuery);
            preparedStatement.setString(1, name);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return rs.getString("Supplier_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int addImportData(int employeeId, String supplierName, float totalPrice) {
        String supplierId = getSupplierIdByName(supplierName);
        Date dateTime = new Date(System.currentTimeMillis());

        String sqlQuery = "INSERT INTO ImportForm (Employee_id, Supplier_id, FormDate, TotalMoney) " +
                "VALUES (?, ?, ?, ?)";

        try {
            PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, employeeId);
            preparedStatement.setInt(2, Integer.parseInt(supplierId));
            preparedStatement.setDate(3, dateTime);
            preparedStatement.setFloat(4, totalPrice);
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public boolean updateProductQuantity(int quantity, int productId) {
        String sqlQuery = "UPDATE Product SET lv1Quantity = lv1Quantity + ? WHERE Product_id = ?";

        try {
            PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement(sqlQuery);
            preparedStatement.setInt(1, quantity);
            preparedStatement.setInt(2, productId);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean autoCreatePaySlip(int employeeId, String content, float totalPay, String status, String note) {
        Date dateTime = new Date(System.currentTimeMillis());

        String sqlQuery = "INSERT INTO PaySlip (Employee_id, Content, CreateDate, TotalPay, Status, Note) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement(sqlQuery);
            preparedStatement.setInt(1, employeeId);
            preparedStatement.setString(2, content);
            preparedStatement.setDate(3, dateTime);
            preparedStatement.setFloat(4, totalPay);
            preparedStatement.setString(5, status);
            preparedStatement.setString(6, note);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ResultSet getProductOffer() {
        String sqlQuery = "SELECT Product_id AS ID, ProductName AS Name, Price, Description, Origin, " +
                "Unit.Unit_Namelv1 AS Unit, TypeName AS Type " +
                "FROM Product, ProductType, Unit " +
                "WHERE Product.ProductType = ProductType.ProductType_id " +
                "AND Unit.Unit_id = Product.Unit_id " +
                "AND lv1Quantity < 5";
        return connectDB.getData(sqlQuery);
    }

    public ResultSet searchProductOffer(String search) {
        String sqlQuery = "SELECT Product_id AS ID, ProductName AS Name, Price, Description, Origin, " +
                "Unit.Unit_Namelv1, TypeName AS Type " +
                "FROM Product, ProductType, Unit " +
                "WHERE Product.ProductType = ProductType.ProductType_id " +
                "AND Unit.Unit_id = Product.Unit_id " +
                "AND (Product_id LIKE ? OR ProductName LIKE ?) " +
                "AND lv1Quantity < 5";

        try {
            PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement(sqlQuery);
            preparedStatement.setString(1, search + "%");
            preparedStatement.setString(2, "%" + search + "%");
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static boolean addImportDetail(DetailImport detailImport) {
        String query = "INSERT INTO DetailImportForm (Product_id, ImportForm_id, ImportPrice, Quantity, Total) VALUES (?, ?, ?, ?, ?)";

        try {
            Connection connection = connectDB.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, Integer.parseInt(detailImport.getProduct_id()));
            preparedStatement.setFloat(3, detailImport.getImportPrice());
            preparedStatement.setInt(4, detailImport.getQuantity());
            preparedStatement.setDouble(5, detailImport.getTotal());

            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static List<DetailImport> getAllImportProducts() {
        List<DetailImport> importProducts = new ArrayList<>();
        String sqlQuery = "SELECT * FROM DetailImportForm"; // Thay đổi truy vấn tùy thuộc vào cấu trúc của cơ sở dữ liệu của bạn

        try (ResultSet rs = connectDB.getData(sqlQuery)) {
            while (rs.next()) {
                DetailImport importProduct = new DetailImport(
                        rs.getString("ProductID"),
                        rs.getString("ProductName"),
                        rs.getFloat("ProductPrice"),
                        rs.getInt("ProductQuantities"),
                        rs.getDouble("ProductTotal")
                );
                importProducts.add(importProduct);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return importProducts;
    }
    public static boolean updateImportDetail(DetailImport detailImport) {
        String sqlQuery = "UPDATE DetailImportForm SET ImportPrice = ?, Quantity = ?, Total = ? WHERE Product_id = ?";

        try (Connection connection = ConnectDB.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setFloat(1, detailImport.getImportPrice());
            preparedStatement.setInt(2, detailImport.getQuantity());
            preparedStatement.setDouble(3, detailImport.getTotal());
            preparedStatement.setString(4, detailImport.getProduct_id());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addImportData(String productId, String productName, float productPrice, int productQuantities, float productTotal, String supplier) {
        String query = "INSERT INTO DetailImportForm (Product_id, ImportForm_id, ImportPrice, Quantity, Total) VALUES (?, ?, ?, ?, ?)";

        try {
            Connection connection = connectDB.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, Integer.parseInt(DetailImport.getProduct_id()));
            preparedStatement.setFloat(3, DetailImport.getImportPrice());
            preparedStatement.setInt(4, DetailImport.getQuantity());
            preparedStatement.setDouble(5, DetailImport.getTotal());

            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean deleteProduct(String productId) {
        String sqlQuery = "DELETE FROM ImportedProducts WHERE ProductId = ?";
        try {
            PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement(sqlQuery);
            preparedStatement.setString(1, productId);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

