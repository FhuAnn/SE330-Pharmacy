package com.example.se330_pharmacy.Controllers;

import com.example.se330_pharmacy.Models.ConnectDB;
import com.example.se330_pharmacy.Models.Model;
import com.example.se330_pharmacy.Models.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import net.synedra.validatorfx.Check;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    public TextField tf_username_forgot;
    public PasswordField tfPassword1_change;
    public PasswordField tfPassword2_change;
    User user;
    private int index ;
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
    void backToLogin(MouseEvent event) {
        index =0;
        ResetTextField();
        forgetPane.toBack();
        changePane.toBack();
    }

    @FXML
    void forgotPasswordOnclick(MouseEvent event) {
        index = 1;
        loginPane.toBack();
        changePane.toBack();
    }

    @FXML
    void sendOTP(MouseEvent event) {

    }

    @FXML
    void btnContinue_clicked(MouseEvent event) throws InterruptedException {
        CheckForFill();
        if(check_otp_username())
        {
            index =2;
            loginPane.toBack();
            forgetPane.toBack();
            pfPassword_Login.setText("");
        }
        else {
            showAlert("Invalid this username + '"+tf_username_forgot.getText().toString()+"'+!");
        }
    }

    private boolean check_otp_username() {
        String username_result = user.getUsername(tf_username_forgot.getText().toString());
        if(username_result==null)
        {
            return false;
        }
        //otp

        //thoã mãn otp và tồn tại username
        return true;
    }

    public void btnConfirm_clicked(MouseEvent mouseEvent) throws SQLException {
        CheckForFill();
        if(UpdatePassword(index))
        {
            showAlert("Password is now changed!");
            index = 0;
            ResetTextField();
            changePane.toBack();
            forgetPane.toBack();
        }
        else showAlert("Error!");

    }
    private boolean UpdatePassword(int index) throws SQLException{
        if(index==0)// yêu cầu đổi mật khẩu mặc định
        {
            return user.UpdatePassword(tfUsername_Login.getText().toString(),tfPassword2_change.getText().toString(),index);
        }
        return user.UpdatePassword(tf_username_forgot.getText().toString(),tfPassword2_change.getText().toString(),index);
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
        user = new User();
        index=0;
    }

    private void loginButtonOnAction()  {
      CheckForFill();
      Login(tfUsername_Login.getText().toString(),pfPassword_Login.getText().toString());
    }

    private void Login(String username,String password) {
        int valid = user.CheckValidate(username,password);
        if(valid==1)
        {
            showAlert("Welcome! Please change your password");
            loginPane.toBack();
            forgetPane.toBack();
        }
        else if(valid == 2)
        {
            showAlert("Login successfully!");
            pfPassword_Login.setText("");
            Stage stage = (Stage) btnLogin.getScene().getWindow(); //get login-screen
            Model.getInstance().getViewFactory().closeStage(stage);//close login-screen
            Model.getInstance().getViewFactory().showMenuWindow(user);//mở menu-screen
        }
        else
            showAlert("Fail to login! Check your Username and Password again");
    }

    public void passwordFieldKeyTyped(KeyEvent keyEvent) {;
        checkValidate20characters(keyEvent);
    }

    private void CheckForFill ()
    {
        if(index == 0) // đang ở màn login
        {
            if((tfUsername_Login.getText().isBlank()) && pfPassword_Login.getText().isBlank())
            {
                showAlert("Please enter username and password");
                tfUsername_Login.setText("");
                pfPassword_Login.setText("");
                return;
            }
        }else
        if(index == 1) // đang ở màn forgotpassword
        {
            if(tf_username_forgot.getText().isBlank()){
                showAlert("You must fill the username!");
                return;
            }
        }
        else // đang ở màn change
        {
            if(tfPassword1_change.getText().isBlank())
            {
                showAlert("You must fill new password");
                return;
            }
            if(tfPassword2_change.getText().isBlank())
            {
                showAlert("You must fill confirm new password");
                return;
            }
            if(!tfPassword2_change.getText().equals(tfPassword1_change.getText()))
            {
                showAlert("Wrong password re-entered, please check again");
            }

        }
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
    private void showAlert(String  string) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(string);
        alert.showAndWait();
    }
    private void ResetTextField()
    {
        tfUsername_Login.setText("");
        pfPassword_Login.setText("");
        tfPassword1_change.setText("");
        tfPassword2_change.setText("");
        tf_username_forgot.setText("");
        textFieldOTP.setText("");
    }
    public  void showLogin()
    {
        Model.getInstance().getViewFactory().showLoginWindow();
    }
}
