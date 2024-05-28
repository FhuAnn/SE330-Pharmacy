package com.example.se330_pharmacy.Controllers;

import com.example.se330_pharmacy.DataAccessObject.EmployeeDAO;
import com.example.se330_pharmacy.Models.Employee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class EmployeeController implements Initializable {
    public Button btnDeleteEmployee;
    public Button btnAddEmployee;
    public Button btnEditEmployee;
    @FXML
    private TextField tfEmployee;
    @FXML
    private TextField addName;
    @FXML
    private TextField addcitizenId;
    @FXML
    private TextField addAddress;
    @FXML
    private TextField addPhoneNum;
    @FXML
    private TextField addEmail;
    @FXML
    private TextField addPosition;
    @FXML
    private TableView<Employee> employeeTableView;
    @FXML
    private TableColumn<Employee, Integer> idColumn;
    @FXML
    private TableColumn<Employee, String> nameColumn;
    @FXML
    private TableColumn<Employee, String> citizenIdColumn;
    @FXML
    private TableColumn<Employee, String> addressColumn;
    @FXML
    private TableColumn<Employee, String> phoneNumColumn;
    @FXML
    private TableColumn<Employee, String> emailColumn;
    @FXML
    private TableColumn<Employee, String> positionColumn;
    @FXML
    private TableColumn<Employee, String> usernameColumn;

    private EmployeeDAO employeeDAO = new EmployeeDAO();
    private ObservableList<Employee> employees;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configureTableColumns();
        loadEmployeeData();
        setOnOffAddDeleteBtn();
        btnDeleteEmployee.setOnAction(event -> handleDeleteAction());
        btnAddEmployee.setOnAction(event -> handleAddAction());
        btnEditEmployee.setOnAction(event -> handleEditAction()); // Add this line to handle edit action
        tfEmployee.setOnKeyPressed(event -> handleSearchKeyPressed(event));
        addSelectionListener(); // Add this line to handle selection changes
    }

    private void setOnOffAddDeleteBtn() {
        btnDeleteEmployee.setDisable(true); // Bắt đầu bằng việc vô hiệu hóa nút Delete
        employeeTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            btnDeleteEmployee.setDisable(newValue == null); // Nếu không có mục nào được chọn, vô hiệu hóa nút Delete
        });
    }

    private void configureTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        citizenIdColumn.setCellValueFactory(new PropertyValueFactory<>("citizenId"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        phoneNumColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        positionColumn.setCellValueFactory(new PropertyValueFactory<>("position"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
    }

    private void loadEmployeeData() {
        employees = FXCollections.observableArrayList(employeeDAO.getAllEmployees());
        employeeTableView.setItems(employees);
    }

    @FXML
    private void handleSearchAction() {
        String searchText = tfEmployee.getText().trim().toLowerCase();
        if (!searchText.isEmpty()) {
            ObservableList<Employee> filteredEmployees = FXCollections.observableArrayList(
                    employees.stream()
                            .filter(emp -> String.valueOf(emp.getEmloyeeId()).toLowerCase().contains(searchText)
                                    || emp.getEmployName().toLowerCase().contains(searchText))
                            .collect(Collectors.toList())
            );
            employeeTableView.setItems(filteredEmployees);
        } else {
            employeeTableView.setItems(employees);
        }
    }

    @FXML
    private void handleDeleteAction() {
        Employee selectedEmployee = employeeTableView.getSelectionModel().getSelectedItem();
        if (selectedEmployee != null) {
            employeeDAO.deleteEmployee(selectedEmployee.getEmloyeeId());
            employees.remove(selectedEmployee);
        }
    }

    @FXML
    private void handleSearchKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            handleSearchAction();
        }
    }

    @FXML
    private void handleAddAction() {
        String name = addName.getText();
        String citizenId = addcitizenId.getText();
        String address = addAddress.getText();
        String phoneNum = addPhoneNum.getText();
        String email = addEmail.getText();
        String position = addPosition.getText();
        String username = addEmail.getText();

        if (!name.isEmpty() && !citizenId.isEmpty() && !address.isEmpty() && !phoneNum.isEmpty() && !email.isEmpty() && !position.isEmpty()) {
            Employee newEmployee = new Employee(name, citizenId, address, phoneNum, email, position, username);
            employeeDAO.addEmployee(newEmployee);
            employees.add(newEmployee);
            clearAddEmployeeFields();
        }
    }

    @FXML
    private void handleEditAction() {
        Employee selectedEmployee = employeeTableView.getSelectionModel().getSelectedItem();
        if (selectedEmployee != null) {
            String name = addName.getText();
            String citizenId = addcitizenId.getText();
            String address = addAddress.getText();
            String phoneNum = addPhoneNum.getText();
            String email = addEmail.getText();
            String position = addPosition.getText();
            String username = addEmail.getText();

            if (!name.isEmpty() && !citizenId.isEmpty() && !address.isEmpty() && !phoneNum.isEmpty() && !email.isEmpty() && !position.isEmpty()) {
                selectedEmployee.setEmployName(name);
                selectedEmployee.setCitizenId(citizenId);
                selectedEmployee.setAddress(address);
                selectedEmployee.setPhoneNumber(phoneNum);
                selectedEmployee.setEmail(email);
                selectedEmployee.setPosition(position);
                selectedEmployee.setUsername(username);

                employeeDAO.updateEmployee(selectedEmployee);
                employeeTableView.refresh();
                clearAddEmployeeFields();
            }
        }
    }

    private void addSelectionListener() {
        employeeTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            boolean employeeSelected = newValue != null;
            btnAddEmployee.setDisable(employeeSelected); // Vô hiệu hóa nút Add khi chọn nhân viên
            if (employeeSelected) {
                // Hiển thị thông tin nhân viên lên các TextField
                addName.setText(newValue.getEmployName());
                addcitizenId.setText(newValue.getCitizenId());
                addAddress.setText(newValue.getAddress());
                addPhoneNum.setText(newValue.getPhoneNumber());
                addEmail.setText(newValue.getEmail());
                addPosition.setText(newValue.getPosition());
            } else {
                clearAddEmployeeFields();
            }
        });
    }

    private void clearAddEmployeeFields() {
        addName.clear();
        addcitizenId.clear();
        addAddress.clear();
        addPhoneNum.clear();
        addEmail.clear();
        addPosition.clear();
    }
}
