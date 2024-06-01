package com.example.se330_pharmacy.DataAccessObject;

import com.example.se330_pharmacy.Models.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ImportDAO {
    private ConnectDB connectDB = ConnectDB.getInstance();

    public ImportDAO() {}

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

    public ObservableList<Product> getProductData() {
        ObservableList<Product> listProduct = FXCollections.observableArrayList();
        String sqlQuery = "SELECT pro.product_id , pro.productname , pro.price_import, pro.description, pro.origin, uni.big_unit, type.typename FROM product pro, producttype type, unit uni " +
                "WHERE pro.producttype_id = type.producttype_id AND uni.unit_id = pro.unit_id";
        try(PreparedStatement statement = connectDB.getPreparedStatement(sqlQuery))
        {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Product product = new Product(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getInt(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getString(6),
                        resultSet.getString(7)
                );
                listProduct.add(product);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listProduct;
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

    public int addImportData(Import _import) {
        Date dateTime = new Date(System.currentTimeMillis());
        String sqlQuery = "INSERT INTO importform (employee_id, supplier_id, formdate, totalmoney) " +
                "VALUES (?, ?, ?, ?) ";

        try {
            PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, _import.getEmployeeId());
            preparedStatement.setInt(2, _import.getSupplierId());
            preparedStatement.setDate(3, dateTime);
            preparedStatement.setFloat(4, _import.getTotal());
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
   /* public boolean addImportDetail(DetailImport detailImport) {
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
    public ObservableList<DetailImport> getAllImportProducts() {
        ObservableList<DetailImport>  importProducts = FXCollections.observableArrayList();
        String sqlQuery = "select product_id as ID, productname as name, price, description, origin, unit as Type from Product , ProductType where Product.ProductType = ProductType.ProductType_id"; // Thay đổi truy vấn tùy thuộc vào cấu trúc của cơ sở dữ liệu của bạn

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
        String sqlQuery = "UPDATE detailimport SET ImportPrice = ?, Quantity = ?, Total = ? WHERE Product_id = ?";

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

    public boolean addImportData(Import _import) {
        String query = "INSERT INTO import (employee_id, partner_id, totalmoney,formdate) VALUES (?, ?, ?, ?)";

        try {
            PreparedStatement preparedStatement = connectDB.getPreparedStatement(query);
            preparedStatement.setInt(1, _import.getEmployeeId());
            preparedStatement.setInt(2, _import.getSupplierId());
            preparedStatement.setInt(4, _import.getTotal());
            preparedStatement.setDate(5, (Date) _import.getFormDate());

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
    }*/
}

