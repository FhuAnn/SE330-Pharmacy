package com.example.se330_pharmacy;

import com.example.se330_pharmacy.Controllers.LoginController;
import com.example.se330_pharmacy.Controllers.MenuController;
import com.example.se330_pharmacy.Models.Model;


import com.example.se330_pharmacy.Models.User;
import javafx.application.Application;


import javafx.stage.Stage;

import java.awt.*;

public class App extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        LoginController loginController = new LoginController();
        loginController.showLogin();
       /* User user = new User();
        MenuController menuController = new MenuController(user);
        menuController.showMenu();*/
    }
}
