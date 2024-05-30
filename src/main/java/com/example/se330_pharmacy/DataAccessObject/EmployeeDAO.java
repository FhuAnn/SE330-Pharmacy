package com.example.se330_pharmacy.DataAccessObject;

import com.example.se330_pharmacy.Models.ConnectDB;
import com.example.se330_pharmacy.Models.Employee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    Employee employee = new Employee();
    ConnectDB connectDB = new ConnectDB();
    public EmployeeDAO() {
    }

    public Employee getEmployee() {
        return employee;
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
                    employee.setEmployeeId(resultSet.getInt("employee_id"));
                    employee.setEmployeeName(resultSet.getString("employname"));
                    employee.setEmployeeCitizenId(resultSet.getString("employee_id"));
                    employee.setEmployeeAddress(resultSet.getString("address"));
                    employee.setEmployeePhoneNumber(resultSet.getString("phonenumber"));
                    employee.setEmployeePosition(resultSet.getString("position"));
                    employee.setEmployeeUsername(resultSet.getString("username"));
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
    public String getEmail(String username) throws SQLException {
        String username_result =null;
        String query = "SELECT email FROM employee WHERE username = '" + username +"'";
        ResultSet resultSet = connectDB.getData(query);
        if(resultSet.next()) // kiểm tra xem resultSet có dữ liệu hay không
        {
            username_result = resultSet.getString("email");
        }
        return username_result;
    }
    public boolean UpdatePassword(String username,String newPassword, int index) throws SQLException
    {
        newPassword = GetHash(newPassword);
        String querry;
        PreparedStatement preparedStatement = null;
        if (index == 0)
        {
            querry = "UPDATE Employee SET Password = ?, DefaultPassword = NULL  WHERE Username = ? ";
            preparedStatement = connectDB.databaseLink.prepareStatement(querry);
            preparedStatement.setString(1,newPassword);
            preparedStatement.setString(2,username);
        }
        else
        {
            querry = "UPDATE Employee SET Password = ? WHERE Username = ? ";
            preparedStatement = connectDB.databaseLink.prepareStatement(querry);
            preparedStatement.setString(1,newPassword);
            preparedStatement.setString(2,username);
        }
        if (connectDB.handleData(preparedStatement))
        {
            return true;
        }
        else
            return false;
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

    public String getUsername(String _username)
    {
        String username =null;
        String query = "SELECT username FROM employee WHERE username = '" + _username +"'";
        ResultSet resultSet = connectDB.getData(query);
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
    public boolean checkID(int id)
    {
        String query = "SELECT * FROM employee WHERE employee_id = '" + id +"'";
        ResultSet resultSet = connectDB.getData(query);
        try
        {
            if(resultSet.next()) // kiểm tra xem resultSet có dữ liệu hay không
            {
                return true;
            }
        }
        catch (SQLException e )
        {
            e.printStackTrace();
        }
        return false;
    }

    public boolean addEmployee(Employee employee) {
        String query = "INSERT INTO employee (employname, phonenumber, citizen_id, username, password, position, defaultpassword, address, email) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String defaultPassword = GeneratePassword(true,true,true,true,8);
        try (PreparedStatement statement = ConnectDB.getInstance().getPreparedStatement(query)) {
            statement.setString(1, employee.getEmployeeUsername());
            statement.setString(2, employee.getEmployeePhoneNumber());
            statement.setString(3, employee.getEmployeeCitizenId());
            statement.setString(4, employee.getEmployeeUsername());
            statement.setString(5, "");
            statement.setString(6, employee.getEmployeePosition());
            statement.setString(7, defaultPassword);
            statement.setString(8, employee.getEmployeeAddress());
            statement.setString(9, employee.getEmployeeEmail());
            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = statement.getGeneratedKeys()) {
                    if (rs.next()) {
                        employee.setEmployeeId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateEmployee(Employee employee) {
        String query = "UPDATE employee SET employname = ?, phonenumber = ?, citizen_id = ?, address = ?, position = ?, email = ? WHERE employee_id = ?";
        try (PreparedStatement statement = ConnectDB.getInstance().getPreparedStatement(query)) {
            statement.setString(1, employee.getEmployeeUsername());
            statement.setString(2, employee.getEmployeePhoneNumber());
            statement.setString(3, employee.getEmployeeCitizenId());
            statement.setString(4, employee.getEmployeeAddress());
            statement.setString(5, employee.getEmployeePosition());
            statement.setString(6, employee.getEmployeeEmail());
            statement.setInt(7, employee.getEmployeeId());
            int aff =  statement.executeUpdate();
            if (aff > 0) return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteEmployee(int employeeId) {
        String query = "DELETE FROM employee WHERE employee_id = ?";
        try (PreparedStatement statement = ConnectDB.getInstance().getPreparedStatement(query)) {
            statement.setInt(1, employeeId);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected>0) return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ObservableList<Employee> getAllEmployees() {
        ObservableList<Employee> employees = FXCollections.observableArrayList();
        String query = "SELECT * FROM employee";
        try (Connection connection = connectDB.databaseLink;
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Employee employee = new Employee();
                employee.setEmployeeId(resultSet.getInt("employee_id"));
                employee.setEmployeeName(resultSet.getString("employname"));
                employee.setEmployeeCitizenId(resultSet.getString("citizen_id"));
                employee.setEmployeePhoneNumber(resultSet.getString("phonenumber"));
                employee.setEmployeeAddress(resultSet.getString("address"));
                employee.setEmployeePosition(resultSet.getString("position"));
                employee.setEmployeeUsername(resultSet.getString("username"));
                employee.setEmployeeEmail(resultSet.getString("email"));
                employees.add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }
}
