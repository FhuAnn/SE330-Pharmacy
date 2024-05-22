package com.example.se330_pharmacy.Views;

import com.example.se330_pharmacy.Controllers.MenuController;
import com.example.se330_pharmacy.Models.Employee;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ViewFactory {

    Stage stageSetting = null;
    Stage stageMenu = null;
    public ViewFactory(){}

    public void showLoginWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/se330_pharmacy/Fxml/Login.fxml"));
        createStage(loader);
    }

    public void showMenuWindow(Employee employee) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/se330_pharmacy/Fxml/Menu.fxml"));
        createStage(loader);
        MenuController menuController = loader.getController();
        menuController.initData(employee);
    }

    public void showReceptionWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/se330_pharmacy/Fxml/Reception.fxml"));
        createStage(loader);
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


}
