package com.example.se330_pharmacy.DataAccessObject;

import com.example.se330_pharmacy.Models.ConnectDB;
import com.example.se330_pharmacy.Models.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
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

    public List<String> getProductTypes() {
        List<String> productTypes = new ArrayList<>();
        String query = "SELECT TypeName FROM ProductType";

        try (Connection connection = connectDB.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(query)) {

            while (rs.next()) {
                productTypes.add(rs.getString("TypeName"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productTypes;
    }

    public List<String> getUnits() {
        List<String> units = new ArrayList<>();
        String query = "SELECT CONCAT(Value, ' ', Unit_Namelv2, '/', Unit_Namelv1) AS Unit FROM Unit";

        try (Connection connection = connectDB.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(query)) {

            while (rs.next()) {
                units.add(rs.getString("Unit"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return units;
    }

    public ResultSet getProductData() {
        String sqlQuery = "SELECT p.Product_id AS ID, p.ProductName AS Name, p.Price, p.Description, p.Origin, " +
                "uni.Unit_Namelv2 AS 'Unit(Small)', uni.Unit_Namelv1 AS 'Unit(Big)', " +
                "pty.TypeName AS Type, uni.Value AS Coef " +
                "FROM Product p, ProductType pty, Unit uni " +
                "WHERE p.ProductType = pty.ProductType_id AND uni.Unit_id = p.Unit_id";
        try {
            Connection connection = new ConnectDB().getConnection();
            Statement statement = connection.createStatement();
            return statement.executeQuery(sqlQuery);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getTypeString(String name) {
        String sqlQuery = "SELECT ProductType_id FROM ProductType WHERE TypeName = ?";
        try (Connection connection = new ConnectDB().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {

            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("ProductType_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getUnitId(String unit1, String unit2, String coef) {
        String sqlQuery = "SELECT Unit_id FROM Unit WHERE Unit_Namelv1 = ? AND Unit_Namelv2 = ? AND Value = ?";
        try (Connection connection = new ConnectDB().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {

            preparedStatement.setString(1, unit1);
            preparedStatement.setString(2, unit2);
            preparedStatement.setInt(3, Integer.parseInt(coef));
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("Unit_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    static String[] CutString(String str) //**
    {
        return str.split("/");
    }

    public boolean addProduct(Product product) {
        String typeid = getTypeString(product.getProductType());
        String[] units = CutString(product.getProductBigUnit());
        String[] smallunit = units[0].split(" ");
        int uni = getUnitId(units[1], smallunit[1], smallunit[0]);

        String sqlQuery = "INSERT INTO Product (ProductName, Price, Description, Origin, ProductType, lv1Quantity, lv2Quantity, Unit_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = connectDB.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {

            preparedStatement.setString(1, product.getProductName());
            preparedStatement.setFloat(2, product.getProductImportPrice());
            preparedStatement.setString(3, product.getProductDescription());
            preparedStatement.setString(4, product.getProductOrigin());
            preparedStatement.setInt(5, Integer.parseInt(typeid));
            preparedStatement.setInt(6, 0);
            if (units[1].equals(smallunit[1])) {
                preparedStatement.setNull(7, Types.INTEGER);
            } else {
                preparedStatement.setInt(7, 0);
            }
            preparedStatement.setInt(8, uni);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteProduct(String id) {
        if (id != null && !id.isEmpty()) {
            String sqlQuery = "DELETE FROM Product WHERE Product_id = ?";
            try (Connection connection = new ConnectDB().getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {

                preparedStatement.setInt(1, Integer.parseInt(id));
                return preparedStatement.executeUpdate() > 0;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean updateProduct(Product product) {
        String typeid = getTypeString(product.getProductType());
        String[] units = CutString(product.getProductBigUnit());
        String[] smallunit = units[0].split(" ");
        int uni = getUnitId(units[1], smallunit[1], smallunit[0]);

        String sqlQuery = "UPDATE Product SET ProductName = ?, Price = ?, Description = ?, Origin = ?, Unit_id = ?, ProductType = ? WHERE Product_id = ?";
        try (Connection connection = connectDB.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {

            preparedStatement.setString(1, product.getProductName());
            preparedStatement.setFloat(2, product.getProductImportPrice());
            preparedStatement.setString(3, product.getProductDescription());
            preparedStatement.setString(4, product.getProductOrigin());
            preparedStatement.setInt(5, uni);
            preparedStatement.setInt(6, Integer.parseInt(typeid));
            preparedStatement.setInt(7, product.getProductId());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getDataQuantity(int productId) {
        ConnectDB connect = new ConnectDB();
        String sqlQuery = "SELECT lv1Quantity FROM Product WHERE ProductId = ?";
        try (Connection connection = connect.getConnection();
             PreparedStatement ps = connection.prepareStatement(sqlQuery)) {
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("lv1Quantity");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Or handle the case where no data is found
    }

    public ResultSet getProductUnit() {
        ConnectDB connect = new ConnectDB();
        String sqlQuery = "SELECT CONCAT(Value, ' ', Unit_Namelv2, '/', Unit_Namelv1) AS Unit, Value AS Coef FROM Unit";
        return connect.getData(sqlQuery);
    }

    public boolean addUnitToDB(String big, String small, String coef) {
        ConnectDB connect = new ConnectDB();
        String sql = "INSERT INTO Unit (Value, Unit_Namelv1, Unit_Namelv2) VALUES (?, ?, ?)";
        try (Connection connection = connect.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(coef));
            ps.setString(2, big);
            ps.setString(3, small);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }




}
