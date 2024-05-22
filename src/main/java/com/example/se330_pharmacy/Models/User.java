package com.example.se330_pharmacy.Models;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.PropertyPermission;
import java.util.Queue;

public class User {
    public String Username ;
    public User()
    {}
    public String Password ;
    public String Employee_id;
    public String EmployName;
    public String Citizen_id;
    public String Email;
    public String PhoneNumber;
    public String Position;
    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }



    public String getCitizen_id() {
        return Citizen_id;
    }

    public void setCitizen_id(String citizen_id) {
        Citizen_id = citizen_id;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String Address;

    public String getUsername() {
        return Username;
    }
    public String getUsername(String _username)
    {
        String username =null;
        ConnectDB connect = new ConnectDB();
        String query = "SELECT username FROM employee WHERE username = '" + _username +"'";
        ResultSet resultSet = connect.getData(query);
        try
        {
            if(resultSet.next()) // kiểm tra xem resultSet có dữ liệu hay không
            {
                username = resultSet.getString("username");
            }
            return username;
        }
        catch (SQLException e )
        {
            e.printStackTrace();
        }
        return null;
    }
    public void setUsername(String username) {
        Username = username;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }
    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
    public String getEmployee_id() {
        return Employee_id;
    }

    public void setEmployee_id(String employee_id) {
        Employee_id = employee_id;
    }

    public String getEmployName() {
        return EmployName;
    }

    public void setEmployName(String employName) {
        EmployName = employName;
    }

    public String getPosition() {
        return Position;
    }

    public void setPosition(String position) {
        Position = position;
    }




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

            ConnectDB connectDB = new ConnectDB();
            password = GetHash(password);
            String query = "SELECT * FROM employee WHERE username = '"+username+"' AND (defaultpassword = '"+password+"' OR password ='"+password+"')";
        try
        {
            //thực thi truy vấn và lấy kết qua
            ResultSet resultSet = connectDB.getData(query);
            //kiểm tra kq trả về
            if (resultSet.next()) {
                //tìm thấy người dùng có user và password khớp
                if (resultSet.getString("defaultpassword")!=null && resultSet.getString("defaultpassword").equals(password))
                    return 1;//mật khẩu mặc định
                else {
                    //fill data
                    filldata(resultSet);
                    return 2; // mật khẩu chính
                }
            } else {
                return 0; // không tìm thấy
            }
        }
        catch (SQLException e )
        {
            e.printStackTrace();
            return -1;
        }
    }

    private void filldata(ResultSet resultSet) throws SQLException {
        setEmployee_id(resultSet.getString("employee_id").toString());
        setEmployName(resultSet.getString("employname").toString());
        setUsername(resultSet.getString("username").toString());
        setPosition(resultSet.getString("position").toString());
        setAddress(resultSet.getString("address").toString());
        setCitizen_id(resultSet.getString("citizen_id").toString());
        setEmail(resultSet.getString("email").toString());
        setPhoneNumber(resultSet.getString("phonenumber").toString());
        setPassword(resultSet.getString("password").toString());
    }

    /*public String UserID(String username,String password) throws SQLException
    {
            ConnectDB connect = new ConnectDB();
            String query = "SELECT employee_id FROM employee WHERE username = '" + username + "' AND password = '" + password + "'";
            return connect.getData(query).getString("employee_id");
    }
    public String UserName(String username,String password) throws SQLException
    {
            ConnectDB connect = new ConnectDB();
            String query = "SELECT employname FROM employee WHERE username = '" + username + "' AND password = '" + password + "'";
            return connect.getData(query).getString("employname");
    }
    public String getUsername(String _username) throws SQLException
    {
        String username =null;
        ConnectDB connect = new ConnectDB();
        String query = "SELECT username FROM employee WHERE username = '" + _username +"'";
        ResultSet resultSet = connect.getData(query);
        if(resultSet.next()) // kiểm tra xem resultSet có dữ liệu hay không
        {
            username = resultSet.getString("username");
        }
        return username;
    }*/
   /* public String getPosition(String username,String password) throws SQLException
    {
            ConnectDB connect = new ConnectDB();
            String query = "SELECT position FROM employee WHERE username = '" + username + "' AND password = '" + password + "'";
            return connect.getData(query).getString("position");
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
    }*/
    public ResultSet LoadListEmployee () throws SQLException {
        ConnectDB connect = new ConnectDB();
        String query = "SELECT employee_id as \"ID\", employname as \"Họ và tên\",citizen_id as \"CCCD\",address as \"Địa chỉ\",phonenumber as \"Số điện thoại\",email as \"Email\",position as \"Vai trò\",username as \"Username\"  FROM employee ";
        return connect.getData(query);
    }

    final String LOWER_CASE = "abcdefghijklmnopqursuvwxyz";
    final String  UPPER_CASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    final String  NUMBERS = "123456789";
    final String  SPECIALS = "!@£$%^&*()#€";

    public String GeneratePassword(boolean useLowercase, boolean useUppercase, boolean useNumbers, boolean useSpecial, int passwordSize)
    {
        char[] _password = new char[passwordSize];
        String charSet = "";
        SecureRandom random = new SecureRandom();
        int counter= 0;
        if (useLowercase) charSet += LOWER_CASE;

        if (useUppercase) charSet += UPPER_CASE;
        if (useNumbers) charSet += NUMBERS;

        if (useSpecial) charSet += SPECIALS;

        for (counter = 0; counter < passwordSize; counter++) {
            _password[counter] = charSet.charAt(random.nextInt(charSet.length()));
        }

        return new String(_password);
    }
    public Boolean AddEmployee(String name, String citizen_id, String address, String phone, String email, String position) throws SQLException
    {
         ConnectDB connectDB = new ConnectDB();
            String _pass = GeneratePassword(true, true, true, false, 6);
            String query = "INSERT INTO employee (employname,citizen_id, address,phonenumber,email,position,username, defaultpassword) VALUES (?,?,?,?,?,?,?,?)";
            PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, citizen_id);
            preparedStatement.setString(3, address);
            preparedStatement.setString(4, phone);
            preparedStatement.setString(5, email);
            preparedStatement.setString(6, position);
            preparedStatement.setString(7, email);
            preparedStatement.setString(8, _pass);

            if (connectDB.handleData(preparedStatement)) {
                return true;
            }
            return false;

    }

    public  boolean DeleteEmployee(String id) throws SQLException
    {
            ConnectDB connectDB = new ConnectDB();
            String query = "DELETE employee WHERE employee_id =?";
            PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement(query);
            preparedStatement.setString(1,id);

            if(connectDB.handleData(preparedStatement))
            {
                return  true;
            }
            return false;
    }
    public Boolean UpdateEmployee(String id, String name, String citizen_id, String address, String phone, String email,String username, String position) throws SQLException
    {
        ConnectDB connectDB = new ConnectDB();

            String _pass = GeneratePassword(true,true,true,false,6);
            String query = "UPDATE employee SET employname = ?,citizen_id =?, address,phonenumber = ?,email=?,position=?,username=? WHERE employee_id = ?)";
            PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement(query);
            preparedStatement.setString(1,name);
            preparedStatement.setString(2,citizen_id);
            preparedStatement.setString(3,address);
            preparedStatement.setString(4,phone);
            preparedStatement.setString(5,email);
            preparedStatement.setString(6,position);
            preparedStatement.setString(7,username);
            preparedStatement.setString(8,id);

            if(connectDB.handleData(preparedStatement))
            {
                return true;
            }
            return false;
    }
    public ResultSet SearchData(String search) throws SQLException //search id or search name
    {
        ConnectDB connect = new ConnectDB();
        String query = STR."SELECT employee_id as \"ID\", employname as \"Họ và tên\",citizen_id as \"CCCD\",address as \"Địa chỉ\",phonenumber as \"Số điện thoại\",email as \"Email\",position as \"Vai trò\",username as \"Username\"  FROM employee WHERE (Employee_id like '\{search}%' or EmployName like N'% \{search}%')";
        return connect.getData(query);
    }
    public boolean UpdatePassword(String username,String newPassword, int index) throws SQLException
    {
        newPassword = GetHash(newPassword);
        String querry;
        ConnectDB connect = new ConnectDB();
        PreparedStatement preparedStatement = null;
        if (index == 0)
        {
            querry = "UPDATE Employee SET Password = ?, DefaultPassword = NULL  WHERE Username = ? ";
            preparedStatement = connect.getConnection().prepareStatement(querry);
            preparedStatement.setString(1,newPassword);
            preparedStatement.setString(2,username);
        }
        else
        {
            querry = "UPDATE Employee SET Password = ? WHERE Username = ? ";
            preparedStatement = connect.getConnection().prepareStatement(querry);
            preparedStatement.setString(1,newPassword);
            preparedStatement.setString(2,username);
        }
        if (connect.handleData(preparedStatement))
        {
            return true;
        }
        else
            return false;
    }
}

