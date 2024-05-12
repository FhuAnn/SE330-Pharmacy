package com.example.se330_pharmacy.Models;

import java.io.IOException;
import java.net.InetAddress;
import java.sql.*;
import javax.swing.*;
public class ConnectDB {
    public Connection databaseLink;
    public ConnectDB()
    {
        databaseLink = getConnection();
    }
    public Connection getConnection(){
       /* String databaseName ="ClinicDB";
        String databaseUser ="postgres";
        String databasePassword="phuan03042004";
        String urlPostgres="jdbc:postgresql://localhost:5432/"+databaseName;*/
        String urlNeon_DB= "jdbc:postgresql://ep-jolly-block-a52e1a3c.us-east-2.aws.neon.tech/PharmacyDB?user=PharmacyDB_owner&password=xKkZe1NrSpq7&sslmode=require";
        try{
            if(isInternetAvailable())
            {
                //Class.forName("com.mysql.cj.jdbc.Driver");
                Class.forName("org.postgresql.Driver");
                /*databaseLink= DriverManager.getConnection(urlPostgres,databaseUser,databasePassword);*/
                databaseLink= DriverManager.getConnection(urlNeon_DB);
                if(databaseLink!=null) System.out.println("Connection Established");
                else System.out.println("Connection Failed");
            }
            else {
                JOptionPane.showMessageDialog(null,"Không có kết nối internet");
            }
        }
        catch (Exception e ){
            e.printStackTrace();
            e.getCause();
        }
       return databaseLink;
    }

    private boolean isInternetAvailable() {
        try {
            InetAddress.getByName("www.google.com").isReachable(3000); // Kiểm tra kết nối tới Google trong 3 giây
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public ResultSet getData(String sqlQuery) throws SQLException {
        PreparedStatement preparedStatement = databaseLink.prepareStatement(sqlQuery);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet;
    }

    public boolean handleData(PreparedStatement preparedStatement) throws SQLException {
        try {
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } finally {
            preparedStatement.close();
        }
    }

    public int getId(PreparedStatement preparedStatement) throws SQLException {
        try {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1); // Assuming the ID is in the first column
            }
            return -1; // Return -1 if no ID found
        } finally {
            preparedStatement.close();
        }
    }

    public ResultSet getResultSet(String sql) throws SQLException {
        Statement statement = databaseLink.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        return resultSet;
    }



}
