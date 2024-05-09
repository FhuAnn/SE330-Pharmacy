package com.example.se330_pharmacy.Models;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    public Connection databaseLink;
    public Connection getConnection(){
        String databaseName ="ClinicDB";
        String databaseUser ="postgres";
        String databasePassword="phuan03042004";
        String urlPostgres="jdbc:postgresql://localhost:5432/"+databaseName;
        //String urlMysql="jdbc:mysql://localhost:5432/"+databaseName;

        try{
            //Class.forName("com.mysql.cj.jdbc.Driver");
            Class.forName("org.postgresql.Driver");
            databaseLink= DriverManager.getConnection(urlPostgres,databaseUser,databasePassword);
            if(databaseLink!=null) System.out.println("Connection Established");
            else System.out.println("Connection Failed");
        }
        catch (Exception e ){
            e.printStackTrace();
            e.getCause();
        }
       return databaseLink;
    }
}
