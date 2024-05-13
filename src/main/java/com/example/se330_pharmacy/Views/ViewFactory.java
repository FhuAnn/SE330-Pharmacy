package com.example.se330_pharmacy.Views;

import com.example.se330_pharmacy.Controllers.MenuController;
import com.example.se330_pharmacy.Controllers.ProfileController;
import com.example.se330_pharmacy.Models.User;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.io.IOException;

public class ViewFactory {

    Stage stageSetting = null;
    Stage stageMenu = null;
    Stage stageProfile = null;
    public ViewFactory(){}

    public void showLoginWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/se330_pharmacy/Fxml/Login.fxml"));
        createStage(loader);
    }

    public void showMenuWindow(User user)  {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/se330_pharmacy/Fxml/Menu.fxml"));
            createStage(loader);
            MenuController menuController = loader.getController();
            menuController.initData(user);
    }

    public void showReceptionWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/se330_pharmacy/Fxml/Reception.fxml"));
        createStage(loader);
    }
    public void showProfileWindow(String id,String name,String username,String pos)
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/se330_pharmacy/Fxml/Profile.fxml"));
        createStage(loader);
        ProfileController profileController = loader.getController();
        profileController.initData(id,name,username,pos);
    }

    private void createStage(FXMLLoader loader) {
        Scene scene = null;
        try {
            scene = new Scene(loader.load());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
    }
    public void closeStage(Stage stage)
    {
        stage.close();
        //khi 1 stage nào đó đóng thì cập nhật tình hình các stage khác
    }
    public void minimizeStage(Stage stage)
    {
        stage.setIconified(true);
    }


}
