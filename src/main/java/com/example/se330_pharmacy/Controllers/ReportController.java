package com.example.se330_pharmacy.Controllers;

import com.example.se330_pharmacy.DataAccessObject.ReportDAO;
import com.example.se330_pharmacy.Models.Report;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class ReportController implements Initializable {
    @FXML
    private TableView<Report> reportTableView;
    @FXML
    private TableColumn<Report, Integer> idColumn;
    @FXML
    private TableColumn<Report, String> medicineNameColumn;
    @FXML
    private TableColumn<Report, Double> purchasePriceColumn;
    @FXML
    private TableColumn<Report, Integer> quantityPurchasedColumn;
    @FXML
    private TableColumn<Report, Double> sellingPriceColumn;
    @FXML
    private TableColumn<Report, Integer> quantitySoldColumn;
    @FXML
    private TableColumn<Report, Double> remainingRateColumn;
    @FXML
    private TableColumn<Report, String> unitColumn;
    @FXML
    private TextField reportTf;

    private ReportDAO reportDAO = new ReportDAO();
    private ObservableList<Report> reports;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configureTableColumns();
        loadReportData();
        setupSearchFunctionality();
    }

    private void configureTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        medicineNameColumn.setCellValueFactory(new PropertyValueFactory<>("medicineName"));
        purchasePriceColumn.setCellValueFactory(new PropertyValueFactory<>("purchasePrice"));
        quantityPurchasedColumn.setCellValueFactory(new PropertyValueFactory<>("quantityPurchased"));
        sellingPriceColumn.setCellValueFactory(new PropertyValueFactory<>("sellingPrice"));
        quantitySoldColumn.setCellValueFactory(new PropertyValueFactory<>("quantitySold"));
        remainingRateColumn.setCellValueFactory(new PropertyValueFactory<>("remainingRate"));
        unitColumn.setCellValueFactory(new PropertyValueFactory<>("unit"));
    }

    private void loadReportData() {
        reports = FXCollections.observableArrayList(reportDAO.getAllReports());
        reportTableView.setItems(reports);
    }

    private void setupSearchFunctionality() {
        reportTf.setOnKeyPressed(this::handleSearchKeyPressed);
    }

    private void handleSearchKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            handleSearchAction();
        }
    }

    private void handleSearchAction() {
        String searchText = reportTf.getText().trim().toLowerCase();
        if (!searchText.isEmpty()) {
            ObservableList<Report> filteredReports = FXCollections.observableArrayList(reportDAO.searchReports(searchText));
            reportTableView.setItems(filteredReports);
        } else {
            reportTableView.setItems(reports);
        }
    }
}
