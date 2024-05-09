package com.example.se330_pharmacy.Controllers;

import com.example.se330_pharmacy.Models.DatabaseConnection;
import com.example.se330_pharmacy.Models.Model;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    public Text loginMessageLabel;
    public TextField tfUsername_Login;
    public PasswordField pfPassword_Login;
    public RadioButton radioHideShow_CP;
    @FXML
    private Button btnChange;

    @FXML
    private Button btnConfirm;

    @FXML
    private Button btnLogin;

    @FXML
    private Pane changePane;

    @FXML
    private Pane forgetPane;

    @FXML
    private Pane loginPane;

    @FXML
    private TextField textFieldOTP;

    @FXML
    private TextField textFieldUsernameCP;

    @FXML
    private Text textWelcome;

    @FXML
    private TextField tfPassword1CP;

    @FXML
    private TextField tfPassword2CP;

    @FXML
    void backToLogin(MouseEvent event) {
        forgetPane.toBack();
        changePane.toBack();
    }

    @FXML
    void forgotPasswordOnclick(MouseEvent event) {
        loginPane.toBack();
        changePane.toBack();
    }

    @FXML
    void sendOTP(MouseEvent event) {

    }

    @FXML
    void btnChangePassword(MouseEvent event) {
        loginPane.toBack();
        forgetPane.toBack();
    }
    @FXML
    void close(MouseEvent event) {
        Stage s = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(s);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnLogin.setOnAction(event -> loginButtonOnAction());
        forgetPane.toBack();
        changePane.toBack();
    }

    private void loginButtonOnAction() {
        if((!tfUsername_Login.getText().isBlank()) && !pfPassword_Login.getText().isBlank())
        {
            ValidateLogin();

        } else {
            showAlert("Please enter username and password");
            tfUsername_Login.setText("");
            pfPassword_Login.setText("");
        }
    }

    private void showAlert(String  string) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(string);
        alert.showAndWait();
    }

    private void ValidateLogin() {
        DatabaseConnection connectionNow = new DatabaseConnection();
        Connection connectionDB= connectionNow.getConnection();
        //Nhập username: "ngocanh0058", password: "abc"
        String verifyLogin = "SELECT COUNT(1) FROM \"NHANVIEN\" WHERE \"TK\" ='"+tfUsername_Login.getText().toString()+"' AND \"MK\" ='"+pfPassword_Login.getText().toString()+"'";
        try {
            Statement statement = connectionDB.createStatement();
            ResultSet queryResult = statement.executeQuery(verifyLogin);
            while(queryResult.next())
            {
                if(queryResult.getInt(1)==1){
                    //loginMessageLabel.setText("Congratulations!");
                    Stage stage = (Stage) btnLogin.getScene().getWindow(); //get login-screen
                    Model.getInstance().getViewFactory().closeStage(stage); //close login-screen
                    Model.getInstance().getViewFactory().showMenuWindow(); //open menu-screen
                    pfPassword_Login.setText("");
                }
                else {
                    showAlert("Invalid login. PLease try login again.");
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            e.getCause();
        }
    }

    public void passwordFieldKeyTyped(KeyEvent keyEvent) {;
        checkValidate20characters(keyEvent);
    }

    private void checkValidate20characters(KeyEvent keyEvent) {
        if (pfPassword_Login.getText().length() >= 20) {
            // Hiển thị thông báo khi độ dài vượt quá 20 ký tự
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Password must be 20 characters or less.");
            alert.showAndWait();
            keyEvent.consume(); // Hủy sự kiện

            // Xoá ký tự vừa nhập dư
            int caretPos = pfPassword_Login.getCaretPosition();
            pfPassword_Login.deleteText(caretPos - 1, caretPos);
        }
    }
}
