package com.example.se330_pharmacy.DataAccessObject;

import com.example.se330_pharmacy.Models.ConnectDB;
import com.example.se330_pharmacy.Models.Employee;

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
    Employee employee = new Employee(name, citizenId, address, phoneNum, email, position, username);

    ConnectDB connectDB = ConnectDB.getInstance();
    public EmployeeDAO() {
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
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
                    employee.setEmloyeeId(resultSet.getInt("employee_id"));
                    employee.setEmployName(resultSet.getString("employname"));
                    employee.setCitizenId(resultSet.getString("employee_id"));
                    employee.setAddress(resultSet.getString("address"));
                    employee.setPhoneNumber(resultSet.getString("phonenumber"));
                    employee.setPosition(resultSet.getString("position"));
                    employee.setUsername(resultSet.getString("username"));
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
            preparedStatement = connectDB.getConnection().prepareStatement(querry);
            preparedStatement.setString(1,newPassword);
            preparedStatement.setString(2,username);
        }
        else
        {
            querry = "UPDATE Employee SET Password = ? WHERE Username = ? ";
            preparedStatement = connectDB.getConnection().prepareStatement(querry);
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

    public void addEmployee(Employee employee) {
        String query = "INSERT INTO nhanvien (hoten, sdt, cccd, username, password, vitri, defaultpassword, diachi, email) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = connectDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, employee.getUsername());
            statement.setString(2, employee.getPhoneNumber());
            statement.setString(3, employee.getCitizenId());
            statement.setString(4, employee.getUsername());
            statement.setString(5, "");
            statement.setString(6, employee.getPosition());
            statement.setString(7, "123");
            statement.setString(8, employee.getAddress());
            statement.setString(9, employee.getEmail());
            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = statement.getGeneratedKeys()) {
                    if (rs.next()) {
                        employee.setEmloyeeId(rs.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateEmployee(Employee employee) {
        String query = "UPDATE nhanvien SET hoten = ?, sdt = ?, cccd = ?, diachi = ?, vitri = ?, email = ? WHERE manv = ?";
        try (Connection connection = connectDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, employee.getUsername());
            statement.setString(2, employee.getPhoneNumber());
            statement.setString(3, employee.getCitizenId());
            statement.setString(4, employee.getAddress());
            statement.setString(5, employee.getPosition());
            statement.setString(6, employee.getEmail());
            statement.setInt(7, employee.getEmloyeeId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteEmployee(int employeeId) {
        String query = "DELETE FROM nhanvien WHERE manv = ?";
        try (Connection connection = connectDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, employeeId);
            int rowsAffected = statement.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        String query = "SELECT * FROM nhanvien";
        try (Connection connection = connectDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Employee employee = new Employee(name, citizenId, address, phoneNum, email, position, username);
                employee.setEmloyeeId(resultSet.getInt("manv"));
                employee.setEmployName(resultSet.getString("hoten"));
                employee.setCitizenId(resultSet.getString("cccd"));
                employee.setPhoneNumber(resultSet.getString("sdt"));
                employee.setAddress(resultSet.getString("diachi"));
                employee.setPosition(resultSet.getString("vitri"));
                employee.setUsername(resultSet.getString("username"));
                employee.setEmail(resultSet.getString("email"));
                employees.add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }

    public Employee getEmployeeById(int employeeId) {
        Employee employee = null;
        String query = "SELECT * FROM nhanvien WHERE manv = ?";
        try (Connection connection = connectDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, employeeId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                employee = new Employee(name, citizenId, address, phoneNum, email, position, username);
                employee.setEmloyeeId(resultSet.getInt("manv"));
                employee.setEmployName(resultSet.getString("hoten"));
                employee.setCitizenId(resultSet.getString("cccd"));
                employee.setPhoneNumber(resultSet.getString("sdt"));
                employee.setPosition(resultSet.getString("vitri"));
                employee.setAddress(resultSet.getString("diachi"));
                employee.setUsername(resultSet.getString("username"));
                employee.setEmail(resultSet.getString("email"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employee;
    }

    public Employee getEmployeeByName(String employeeName) {
        Employee employee = null;
        String query = "SELECT * FROM nhanvien WHERE hoten = ?";
        try (Connection connection = connectDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, employeeName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                employee = new Employee(name, citizenId, address, phoneNum, email, position, username);
                employee.setEmloyeeId(resultSet.getInt("manv"));
                employee.setEmployName(resultSet.getString("hoten"));
                employee.setCitizenId(resultSet.getString("cccd"));
                employee.setPhoneNumber(resultSet.getString("sdt"));
                employee.setPosition(resultSet.getString("vitri"));
                employee.setAddress(resultSet.getString("diachi"));
                employee.setUsername(resultSet.getString("username"));
                employee.setEmail(resultSet.getString("email"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employee;
    }
}
