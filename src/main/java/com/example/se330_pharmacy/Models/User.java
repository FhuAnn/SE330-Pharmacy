package com.example.se330_pharmacy.Models;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User {
    public String Username;
    public String Password;
    public String Employee_id;
    public String EmployName;
    public String Citizen_id;
    public String Address;
    public String Email;
    public String PhoneNumber;
    public String Position;


    public String GetHash(String plainText) {
        try {
            // Khởi tạo đối tượng MessageDigest với thuật toán MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // Chuyển đổi chuỗi thành mảng bytes và băm bằng MD5
            byte[] messageDigest = md.digest(plainText.getBytes());

            // Chuyển đổi mảng bytes thành chuỗi hex để hiển thị kết quả
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            System.err.println("Thuật toán MD5 không được hỗ trợ.");
            e.printStackTrace();
            return null;
        }
    }

    public int CheckValidate(String username, String password) {
        try {
            ConnectDB connectDB = new ConnectDB();
            password = GetHash(password);
            String query = "SELECT * FROM employee WHERE username = '"+username+"' AND (defaultpassword = '\"+password+\"' OR password ='\"+password+\"')";
            //thực thi truy vấn và lấy kết qua
            ResultSet resultSet = connectDB.getData(query);

            //kiểm tra kq trả về
            if (resultSet.next()) {
                //tìm thấy người dùng có user và password khớp
                if (resultSet.getString("defaultpassword").equals(password))
                    return 1;//mật khẩu mặc định
                else {
                    return 2; // mật khẩu chính
                }
            } else {
                return 0; // không tìm thấy
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
    public String UserID(String username,String password)
    {
        try {
            ConnectDB connect = new ConnectDB();
            String query = "SELECT employee_id FROM employee WHERE username = '" + username + "' AND password = '" + password + "'";
            return connect.getData(query).getString("employee_id");

        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }
    public String UserName(String username,String password)
    {
        try {
            ConnectDB connect = new ConnectDB();
            String query = "SELECT employname FROM employee WHERE username = '" + username + "' AND password = '" + password + "'";
            return connect.getData(query).getString("employname");

        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }
    public String getPosition(String username,String password)
    {
        try {
            ConnectDB connect = new ConnectDB();
            String query = "SELECT position FROM employee WHERE username = '" + username + "' AND password = '" + password + "'";
            return connect.getData(query).getString("position");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }
    // Forgot password
    public String getEmail(String username,String password)
    {
        try {
            ConnectDB connect = new ConnectDB();
            String query = "SELECT email FROM employee WHERE username = '" + username + "' AND password = '" + password + "'";
            return connect.getData(query).getString("email");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }
    public ResultSet LoadListEmployee () throws SQLException {
        ConnectDB connect = new ConnectDB();
        String query = "SELECT employee_id as \"ID\", employname as \"Họ và tên\",citizen_id as \"CCCD\",address as \"Địa chỉ\",phonenumber as \"Số điện thoại\",email as \"Email\",position as \"Vai trò\",username as \"Username\"  FROM employee ";
        return connect.getData(query);
    }
}
