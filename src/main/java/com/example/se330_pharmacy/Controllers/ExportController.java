package com.example.se330_pharmacy.Controllers;

import com.example.se330_pharmacy.DataAccessObject.ProductDAO;
import com.example.se330_pharmacy.Models.Employee;
import com.example.se330_pharmacy.Models.ExportItem;
import com.example.se330_pharmacy.Models.Product;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ExportController implements Initializable {

    private ProductDAO productDAO = new ProductDAO();
    private Employee _employee;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnCreateForm1;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnEdit;

    @FXML
    private TextArea taExportReason;

    @FXML
    private TableColumn<Product, String> tcProductDescription;

    @FXML
    private TableColumn<Product, Integer> tcProductId;

    @FXML
    private TableColumn<ExportItem, Integer> tcProductIdE;

    @FXML
    private TableColumn<Product, String> tcProductName;

    @FXML
    private TableColumn<ExportItem, String> tcProductNameE;

    @FXML
    private TableColumn<Product, String> tcProductOrigin;

    @FXML
    private TableColumn<Product, Long> tcProductPrice;

    @FXML
    private TableColumn<ExportItem, Long> tcProductPriceE;

    @FXML
    private TableColumn<ExportItem, Integer> tcProductQuanE;

    @FXML
    private TableColumn<Product, String> tcProductType;

    @FXML
    private TableColumn<Product, String> tcProductUnit;

    @FXML
    private TableColumn<ExportItem, Long> tcTotalValueE;

    @FXML
    private Text textTotalValue;

    @FXML
    private TextField tfProductID;

    @FXML
    private TextField tfProductName;

    @FXML
    private TextField tfProductPrice;

    @FXML
    private TextField tfProductQuantities;

    @FXML
    private TextField tfProductTotal;

    @FXML
    private TableView<ExportItem> tvExportForm;

    @FXML
    private TableView<Product> tvProductTable;
    final private int FIXED_QUANTITY = 10;
    private int productQuantities;

    @FXML
    void btnAddClicked(MouseEvent event) {

    }

    @FXML
    void btnCancelClicked(MouseEvent event) {

    }

    @FXML
    void btnCreateFormClicked(MouseEvent event) {

    }

    @FXML
    void btnDeleteClicked(MouseEvent event) {

    }

    @FXML
    void btnEditClicked(MouseEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tfProductID.setDisable(true);
        tfProductName.setDisable(true);
        tfProductTotal.setDisable(true);
        setProductTable();
        setUpProductRowClick();
        tfProductQuantities.textProperty().addListener((observable, oldValue, newValue) -> updateTotalValue());
        tfProductPrice.textProperty().addListener((observable, oldValue, newValue) -> updateTotalValue());
    }

    private void updateTotalValue() {
        try {
            int quantity = Integer.parseInt(tfProductQuantities.getText());
            double price = Double.parseDouble(tfProductPrice.getText());
            double totalValue = quantity * price;
            tfProductTotal.setText(String.valueOf(totalValue));
        } catch (NumberFormatException e) {
            // Xử lý nếu người dùng nhập không hợp lệ (không phải số)
            tfProductTotal.setText("Invalid input");
        }
    }


    private void setUpProductRowClick() {
        btnAdd.setDisable(false);
        tvProductTable.setRowFactory(tv -> {
            TableRow<Product> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    Product productSelected = row.getItem();

                    if(productSelected!=null && productQuantities < FIXED_QUANTITY) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION,"Sản phẩm này có lượng hàng tồn kho khá ít. Hãy nhập thêm sản phẩm!", ButtonType.OK,ButtonType.CANCEL);
                        alert.showAndWait();

                    } else {
                        tfProductID.setText(STR."\{productSelected.getProductId()}");
                        tfProductName.setText(productSelected.getProductName());
                        tfProductPrice.setText(STR."\{productSelected.getProductImportPrice()}");
                    }
                }
            });
            return row;
        });

    }

    private void setProductTable() {
        bindProductData();
        ObservableList<Product> products = productDAO.getAllProductExport();
        tvProductTable.setItems(products);
        productQuantities = products.get(0).getProductBigUnitQuantities();
        tvProductTable.setRowFactory(tv -> new TableRow<Product>() {
            @Override
            protected void updateItem(Product product,boolean empty) {
                super.updateItem(product,empty);
                if(product!=null && product.getProductBigUnitQuantities() < FIXED_QUANTITY) {
                    setStyle("-fx-background-color: tomato;");
                }
            }
        });
    }

    private void bindProductData() {
        tcProductId.setCellValueFactory(new PropertyValueFactory<>("productId"));
        tcProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        tcProductPrice.setCellValueFactory(new PropertyValueFactory<>("productImportPrice"));
        tcProductDescription.setCellValueFactory(new PropertyValueFactory<>("productDescription"));
        tcProductOrigin.setCellValueFactory(new PropertyValueFactory<>("productOrigin"));
        tcProductUnit.setCellValueFactory(new PropertyValueFactory<>("productBigUnit"));
        tcProductType.setCellValueFactory(new PropertyValueFactory<>("productType"));
    }

}
