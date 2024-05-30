package com.example.se330_pharmacy.Models;


import java.sql.ResultSet;
import java.sql.SQLException;

public class Employee {
    public int employeeId;
    public String employeeName;
    public String employeeCitizenId;
    public String employeeAddress;
    public String employeePhoneNumber;
    public String employeeEmail;
    public String employeePosition;
    public String username;
    public String employeeUsername;
    public String employeePassword;
    public String defaultpassword;

    @Override
    public String toString() {
        return "Employee{" +
                "employeeId=" + employeeId + '\'' +
                ", employeeName='" + employeeName + '\'' +
                ", employeeCitizenId='" + employeeCitizenId + '\'' +
                ", employeeAddress='" + employeeAddress + '\'' +
                ", employeePhoneNumber=" + employeePhoneNumber +
                ", employeeEmail='" + employeeEmail + '\'' +
                ", employeePosition=" + employeePosition + '\'' +
                ", employeeUsername=" + employeeUsername +
                '}';
    }

    public Employee(String name, String citizenId, String address, String phoneNum, String email, String position, String username) {
        this.employeeName = employeeName;
        this.employeeCitizenId = employeeCitizenId;
        this.employeeAddress = employeeAddress;
        this.employeePhoneNumber = employeePhoneNumber;
        this.employeeEmail = employeeEmail;
        this.employeePosition = employeePosition;
        this.employeeUsername = employeeUsername;
    }

    public int getEmloyeeId() {
        return employeeId;
    }

    public void setEmloyeeId(int emloyeeId) {
        this.employeeId = emloyeeId;
    }

    public String getEmployName() {
        return employeeName;
    }

    public void setEmployName(String employName) {
        this.employeeName = employName;
    }

    public String getCitizenId() {
        return employeeCitizenId;
    }

    public void setCitizenId(String citizenId) {
        this.employeeCitizenId = citizenId;
    }

    public String getAddress() {
        return employeeAddress;
    }

    public void setAddress(String address) {
        this.employeeAddress = address;
    }

    public String getPhoneNumber() {
        return employeePhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.employeePhoneNumber = phoneNumber;
    }

    public String getEmail() {
        return employeeEmail;
    }

    public void setEmail(String email) {
        this.employeeEmail = email;
    }

    public String getPosition() {
        return employeePosition;
    }

    public void setPosition(String position) {
        this.employeePosition = position;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return employeePassword;
    }

    public void setPassword(String password) {
        this.employeePassword = password;
    }

    public String UserID(String username,String password) throws SQLException
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
    }
    public String getPosition(String username,String password) throws SQLException
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
    }
    public ResultSet LoadListEmployee () throws SQLException {
        ConnectDB connect = new ConnectDB();
        String query = "SELECT employee_id as \"ID\", employname as \"Họ và tên\",citizen_id as \"CCCD\",address as \"Địa chỉ\",phonenumber as \"Số điện thoại\",email as \"Email\",position as \"Vai trò\",username as \"Username\"  FROM employee ";
        return connect.getData(query);
    }


   /* public Boolean AddEmployee(String name, String citizen_id, String address, String phone, String email, String position) throws SQLException
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
    public ResultSet SearchData (String search) throws SQLException //search id or search name
    {
        ConnectDB connect = new ConnectDB();
        String query = STR."SELECT employee_id as \"ID\", employname as \"Họ và tên\",citizen_id as \"CCCD\",address as \"Địa chỉ\",phonenumber as \"Số điện thoại\",email as \"Email\",position as \"Vai trò\",username as \"Username\"  FROM employee WHERE (Employee_id like '\{search}%' or EmployName like N'% \{search}%')";
        return connect.getData(query);
    }
*/
}

