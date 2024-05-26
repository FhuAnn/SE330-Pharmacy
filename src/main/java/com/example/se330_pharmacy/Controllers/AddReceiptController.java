package com.example.se330_pharmacy.Controllers;

import com.example.se330_pharmacy.DataAccessObject.PayslipDAO;
import com.example.se330_pharmacy.DataAccessObject.ReceiptDAO;
import com.example.se330_pharmacy.Models.Model;
import com.example.se330_pharmacy.Models.Payslip;
import com.example.se330_pharmacy.Models.Receipt;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.swing.*;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AddReceiptController implements Initializable {

    public ComboBox<String> cbStatusReceipt;
    public Button btnExport;
    public Button btnCancel;
    public Text lbl_createDate;
    public Text lbl_totalPay;
    public Text lbl_thangLuong;
    public Text lbl_hoTen;
    public Text lbl_maNhanVien;
    public Text lbl_thongTinYours;
    public Text lbl_viTri;
    public TextArea ta_content;
    public TextArea ta_note;
    ReceiptController receiptController ;
    ReceiptDAO receiptDAO ;
    PayslipDAO payslipDAO;
    Payslip payslip;
    int idCharger;
    PaySlipController paySlipController_init;
    public void initData(Payslip _payslip,int _idCharger,String _employnameCharger,String _vitricharger,PaySlipController paySlipController)
    {
        payslip=_payslip;
        idCharger=_idCharger;
        paySlipController_init = paySlipController;

        lbl_thongTinYours.setText(_idCharger+" - " + _employnameCharger+ " - " + _vitricharger);
        lbl_maNhanVien.setText(String.valueOf(_payslip.getEmployee_id()));
        lbl_hoTen.setText(_payslip.getTenNhanVien());
        lbl_viTri.setText(_payslip.getViTriLamViec());

        lbl_thangLuong.setText(String.valueOf(_payslip.getCreateDate().toLocalDate().getMonthValue()));
        lbl_createDate.setText(String.valueOf(LocalDate.now()));
        lbl_totalPay.setText(String.valueOf(_payslip.getTotalPay()));

        ta_content.setText("Thanh toán lương tháng "+lbl_thangLuong.getText()+ " cho nhân viên: "+ _payslip.getReceipt_id() +_payslip.getEmployee_id()+"-"+_payslip.getTenNhanVien());
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setUp();
        setAction();
    }

    private void setUp() {
        receiptController = new ReceiptController();
        receiptDAO = new ReceiptDAO();
        payslipDAO = new PayslipDAO();
        ObservableList<String> statusList = FXCollections.observableArrayList("InComplete", "Completed");
        cbStatusReceipt.setItems(statusList);
    }

    private void setAction() {
        btnExport.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(CheckFilled()) {
                    CreateReceipt();
                }
            }
        });
        btnCancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                    Stage stage = (Stage) btnCancel.getScene().getWindow();
                    Model.getInstance().getViewFactory().closeStage(stage);
            }
        });
    }
    private boolean CheckFilled() { // tra ve 0 khi khong ton tai textfield nao duoc fill

        if(ta_content.getText().isEmpty()) {
            showAlert("Warning","Chưa nhập đầy đủ thông tin!");
            return false;
        };
        if(cbStatusReceipt.getValue()==null) {
            showAlert("Warning","Chưa nhập đầy đủ thông tin!");
            return false;
        }
        return true;
    }
    private void CreateReceipt() {
        Receipt receipt = new Receipt();
        receipt.setEmployee_id(payslip.getEmployee_id());
        receipt.setContent(ta_content.getText());
        receipt.setCreateDate(Date.valueOf(LocalDate.now()));
        receipt.setTotalPay(payslip.getTotalPay());
        receipt.setNote(ta_note.getText());
        receipt.setStatus(cbStatusReceipt.getValue().toString());
        receipt.setPersoncharge_id(idCharger);
        receipt.setPayslip_id(payslip.getPayslip_id());
        if(receiptDAO.AddReceiptToDB(receipt)>-1) {
            payslipDAO.UpdatePayslipCompleted(payslip.getPayslip_id());
            paySlipController_init.LoadListPayslip();
            showAlert("Warning","Thêm dữ liệu vào database thành công");
            Model.getInstance().getViewFactory().closeStage((Stage) btnExport.getScene().getWindow());
        }
    }
    @FXML
    void close(MouseEvent event) {
        Stage s = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(s);
    }
    private int ShowYesNoAlert(String string) {
        JFrame frame = new JFrame("Table Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        return JOptionPane.showConfirmDialog(frame, "Are you sure you want to "+string+" this row?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
    }
    private void showAlert(String tilte,String string) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(tilte);
        alert.setHeaderText(null);
        alert.setContentText(string);
        alert.showAndWait();
    }
}
