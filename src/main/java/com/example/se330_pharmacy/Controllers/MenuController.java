package com.example.se330_pharmacy.Controllers;

import com.example.se330_pharmacy.Models.Model;
import com.example.se330_pharmacy.Models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.util.Optional;

public class MenuController {

    private User user;
    @FXML
    private Button btnEmployee;

    @FXML
    private Button btnExport;

    @FXML
    private Button btnImport;

    @FXML
    private Button btnLogout;

    @FXML
    private MenuItem btnPayslip;

    @FXML
    private Button btnProduct;

    @FXML
    private MenuItem btnReceipt;

    @FXML
    private Button btnReport;

    @FXML
    private Button btnSale;

    @FXML
    private  Pane mainPane;

    @FXML
    private Text titleTextField;

    public MenuController()
    {
        this.user = new User();
    }

    @FXML
    void btnEmployeeClicked(ActionEvent event) throws IOException {
        setMainPane("/com/example/se330_pharmacy/Fxml/Employee.fxml");
        titleTextField.setText("Employee");
    }

    @FXML
    void btnExportClicked(ActionEvent event) throws IOException {
        setMainPane("/com/example/se330_pharmacy/Fxml/Export.fxml");
        titleTextField.setText("Export");
    }

    @FXML
    void btnImportClicked(ActionEvent event) throws IOException {
        titleTextField.setText("Import");
        setMainPane("/com/example/se330_pharmacy/Fxml/Import.fxml");
    }

    @FXML
    void btnLogoutClicked(MouseEvent event) {
        closeMenu(event);
    }

    @FXML
    void btnPayslipClicked(ActionEvent event) throws IOException {
        titleTextField.setText("Payslip");
        setMainPane("/com/example/se330_pharmacy/Fxml/Accountant_PaySlip.fxml");
    }

    @FXML
    void btnProductCicked(ActionEvent event) throws IOException {
        titleTextField.setText("Sản phẩm");
        setMainPane("/com/example/se330_pharmacy/Fxml/Product.fxml");
    }

    @FXML
    void btnReceiptClicked(ActionEvent event) throws IOException {
        titleTextField.setText("Hóa đơn");
        setMainPane("/com/example/se330_pharmacy/Fxml/Accountant_Receipt.fxml");
    }

    @FXML
    void btnSaleClicked(ActionEvent event) throws IOException {
        titleTextField.setText("Bán hàng");
        setMainPane("/com/example/se330_pharmacy/Fxml/Sale.fxml");
    }
    
    @FXML
    void btnReportClicked(ActionEvent event) throws IOException {
        titleTextField.setText("Báo cáo");
        setMainPane("/com/example/se330_pharmacy/Fxml/Report.fxml");
    }

    @FXML
    void closeMenu(MouseEvent event) {
        Stage s = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("DangXuat");
        confirmationAlert.setContentText("Ban muon dang xuat?");

        ButtonType okButton = new ButtonType("OK");
        ButtonType cancelButton = ButtonType.CANCEL;

        confirmationAlert.getButtonTypes().setAll(okButton, cancelButton);

        Optional<ButtonType> result = confirmationAlert.showAndWait();

        if (result.isPresent() && result.get() == okButton) {
            Model.getInstance().getViewFactory().closeStage(s);
        }
    }
    public void initData(User _user)
    {
        this.user=_user;
    }
    public void ProfileEmploy_Clicked(MouseEvent mouseEvent) {
        String id = this.user.Employee_id;
        String name = this.user.getEmployName();
        String username = this.user.getUsername();
        String pos = this.user.getPosition();
        Model.getInstance().getViewFactory().showProfileWindow(id,name,username,pos);
    }
    @FXML
    void minimizeMenu(MouseEvent event) {
        Stage s = (Stage) ((Node)event.getSource()).getScene().getWindow();
        s.setIconified(true);
    }
    private void setMainPane(String resource) throws IOException {
        mainPane.getChildren().clear();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(resource));
        Parent reportSceneRoot = loader.load();
        mainPane.getChildren().add(reportSceneRoot);
    }

}
