package com.example.se330_pharmacy.Views;

import com.example.se330_pharmacy.Controllers.*;
import com.example.se330_pharmacy.DataAccessObject.PayslipDAO;
import com.example.se330_pharmacy.Models.Employee;
import com.example.se330_pharmacy.Models.Payslip;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ViewFactory {

    Stage stageSetting = null;
    Stage stageMenu = null;
    Stage stageAddReceiptAccountant = null;
    Stage stageAddPayslipAccountant = null;
    Stage stageProfile =null;
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

    public void showAddReceiptWindow(Payslip payslip,int _idCharger,String _employnameCharger,String _vitricharger,PaySlipController paySlipController) {
        if(stageAddReceiptAccountant==null)
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/se330_pharmacy/Fxml/Add_Accountant_Receipt.fxml"));
            stageAddReceiptAccountant=createStage(loader);
            AddReceiptController addReceiptController = loader.getController();
            addReceiptController.initData(payslip,_idCharger,_employnameCharger,_vitricharger,paySlipController);
        }
        else {
            stageAddReceiptAccountant.toFront();
        }

    }
    public void showAddPayslipWindow(Payslip payslip, PaySlipController paySlipController) {
        if(stageAddPayslipAccountant==null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/se330_pharmacy/Fxml/Add_Accountant_Payslip.fxml"));
            stageAddPayslipAccountant = createStage(loader);
            AddPayslipController addPayslipController = loader.getController();
            addPayslipController.initData(payslip,paySlipController);
        } else  stageAddPayslipAccountant.toFront();
    }
    private Stage createStage(FXMLLoader loader) {
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
        return stage;
    }
    public void closeStage(Stage stage)
    {
        stage.close();
        if(stageMenu!=null &&!stageMenu.isShowing()) // nếu menu đóng thì đóng hết những window đang mở
        {
            if(stageAddReceiptAccountant!=null) stageAddReceiptAccountant.close();
            if(stageAddPayslipAccountant!=null) stageAddPayslipAccountant.close();
            //if(stageSetting!=null) stageSetting.close();
            //if(stageProfile!=null) stageProfile.close();
        }
        //khi 1 stage nào đó đóng thì cập nhật tình hình các stage khác
        if(stageAddReceiptAccountant!=null && !stageAddReceiptAccountant.isShowing()) stageAddReceiptAccountant=null;  // nếu khác null nhưng ko còn show thì cập nhật về null
        if(stageAddPayslipAccountant!=null && !stageAddPayslipAccountant.isShowing()) stageAddPayslipAccountant=null;
        //if(stageSetting!=null && !stageSetting.isShowing()) stageSetting=null;
        //if(stageProfile!=null &&!stageProfile.isShowing()) stageProfile=null;
    }


}
