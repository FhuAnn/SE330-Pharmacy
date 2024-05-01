package com.example.se330_pharmacy.Controllers;

import com.example.se330_pharmacy.Models.Model;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

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
        s.close();
    }

    @FXML
    void minimize(MouseEvent event) {
        Stage s = (Stage) ((Node)event.getSource()).getScene().getWindow();
        s.setIconified(true);
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnLogin.setOnAction(event -> Model.getInstance().getViewFactory().showMenuWindow());
        forgetPane.toBack();
        changePane.toBack();
    }
}
