package com.example.se330_pharmacy.Controllers;

import com.example.se330_pharmacy.DataAccessObject.ReportDAO;
import com.example.se330_pharmacy.Models.Bill;
import com.example.se330_pharmacy.Models.Report;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.Normalizer;
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ReportController implements Initializable {

    public DatePicker dp_date;
    public TabPane tabpaneReportMain;
    public Tab tabStatus,tabTop;
    public TableView<Report> tbl_reportStatus;
    public TableColumn<Report, Integer> col_id;
    public TableColumn<Report, String> col_date;
    public TableColumn<Report, String> col_name;
    public TableColumn<Report, String> col_value;
    public TableView<Report> tbl_reportTop;
    public TableColumn<Report, Integer> col_idTop;
    public TableColumn<Report, String> col_nameTop;
    public TableColumn<Report, String> col_numberTOP;
    public TableColumn<Report, Integer> col_totalTop;
    public TableView<Bill> tbl_reportMedicine;
    public TableColumn<Bill, Integer> col_idMedicine;
    public TableColumn<Bill, String> col_nameMedicine;
    public TableColumn<Bill, Integer> col_number;
    public Text numberBillOfMonth;
    public Text numberBillOfToday;
    public Text numberProductOfMonth;
    public Text numberProductOfDay;
    public Text valueRevenueMonth;
    public Text valueRevenueToday;
    public Text valueImportMonth;
    public Text valueImportToday;
    public Text valueExportMonth;
    public Text valueExportToday;
    public CheckBox checkboxAuto;
    private Timeline timeline;
    public TextField tfSearch;
    public Button btnRefresh;
    private ReportDAO reportDAO = new ReportDAO();
    private ObservableList<Report> reportStatus,reportTop;
    private ObservableList<Bill> reportMedicine;
    SingleSelectionModel<Tab> selectionModel ;
    int time_remaining = 300;
    public BorderPane borderPane;
    public Pane paneProgress;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SetUp();
        configureTableColumns();
        loadReportData();
    }

    private void SetUp() {
        tabpaneReportMain.getSelectionModel().select(tabStatus);
        reportStatus = FXCollections.observableArrayList();
        reportTop = FXCollections.observableArrayList();
        reportMedicine = FXCollections.observableArrayList();
        dp_date.setValue(LocalDate.now());
        tfSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.isEmpty()) {
                selectionModel = tabpaneReportMain.getSelectionModel();
//                if(selectionModel.getSelectedItem()==tabStatus) {
//                    tbl_reportStatus.setItems(searchStatus(tfSearch.getText()));
//                } else {
//                    tbl_reportTop.setItems(searchEmployeeTop(tfSearch.getText()));
//                }
            } else {
                tbl_reportStatus.setItems(reportStatus);
                tbl_reportTop.setItems(reportTop);
            }
        });
        dp_date.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                loadReportData();
            }
        });
        checkboxAuto.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(checkboxAuto.isSelected()) {
                    dp_date.setValue(LocalDate.now());
                    btnRefresh.setVisible(false);
                    timeline = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            time_remaining--;
                           checkboxAuto.setText("Làm mới tự động sau " + (int)(time_remaining - 2)+ " s");
                            // lbl_send_otp.setX();
                            if (time_remaining == 5) {
                                checkboxAuto.setText("Đang làm mới.....");
                                loadReportData();
                            }
                            if(time_remaining==0) {
                                time_remaining=300;
                            }
                        }
                    }));
                    timeline.setCycleCount(Timeline.INDEFINITE);
                    timeline.play();
                } else {
                    btnRefresh.setVisible(true);
                    checkboxAuto.setText("Làm mới tự động");
                    timeline.stop();
                }
            }
        });
        btnRefresh.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                loadReportData();
            }
        });
//        LoadProductBarChart();
    }




    private void configureTableColumns() {
        col_id.setCellValueFactory(new PropertyValueFactory<>("idBillStatus"));
        col_date.setCellValueFactory(new PropertyValueFactory<>("dateStatus"));
        col_name.setCellValueFactory(new PropertyValueFactory<>("employnameStatus"));
        col_value.setCellValueFactory(new PropertyValueFactory<>("valueStatus"));

        col_idTop.setCellValueFactory(new PropertyValueFactory<>("idEmployTop"));
        col_nameTop.setCellValueFactory(new PropertyValueFactory<>("employnameTop"));
        col_numberTOP.setCellValueFactory(new PropertyValueFactory<>("numberTop"));
        col_totalTop.setCellValueFactory(new PropertyValueFactory<>("totalTop"));

        col_idMedicine.setCellValueFactory(new PropertyValueFactory<>("productId"));
        col_nameMedicine.setCellValueFactory(new PropertyValueFactory<>("productName"));
        col_number.setCellValueFactory(new PropertyValueFactory<>("quantities"));
    }

    private void loadReportData() {
        paneProgress.setVisible(true);
        new Thread(()->{
            try {
                Thread.sleep(1000);
                Platform.runLater(() -> {
                        LocalDate date = dp_date.getValue();
                        int day = date.getDayOfMonth();
                        int month = date.getMonthValue();
                        int year =date.getYear();
                        reportStatus = reportDAO.getReportStatus(month,year);
                        reportTop=reportDAO.getReportTop(month,year);
                        reportMedicine= reportDAO.GetTop10ProductData(month,year);
//                        LoadChar();
                        tbl_reportStatus.setItems(reportStatus);
                        tbl_reportTop.setItems(reportTop);
                        tbl_reportMedicine.setItems(reportMedicine);
                        try {
                            numberBillOfMonth.setText(reportDAO.GetNumberOfBillMonth(month,year));
                            numberBillOfToday.setText(reportDAO.GetNumberOfBillToday(day,month,year));
                            numberProductOfMonth.setText(reportDAO.GetNumberOfProductMonth(month,year));
                            numberProductOfDay.setText(reportDAO.GetNumberOfProductToday(day,month,year));
                            valueRevenueMonth.setText(("+ "+reportDAO.GetRevenueFromSaleMonth(month,year)+" VND"));
                            valueRevenueToday.setText(("+ "+reportDAO.GetRevenueFromSaleToday(day,month,year)+" VND"));
                            valueImportMonth.setText("- "+reportDAO.GetRevenueImportMonth(month,year)+" VND");
                            valueImportToday.setText("- "+reportDAO.GetRevenueImportDay(day,month,year)+" VND");
                            valueExportMonth.setText("+ "+reportDAO.GetExpendExportMonth(month,year)+" VND");
                            valueExportToday.setText("+ "+reportDAO.GetExpendExportDay(day,month,year)+" VND");
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                });
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                Platform.runLater(() -> {
                    paneProgress.setVisible(false);
                });
            }
        }).start();

    }


}
