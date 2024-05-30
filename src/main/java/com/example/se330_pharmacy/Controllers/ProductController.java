package com.example.se330_pharmacy.Controllers;

import com.example.se330_pharmacy.DataAccessObject.ProductDAO;
import com.example.se330_pharmacy.Models.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static com.almasb.fxgl.dsl.FXGLForKtKt.showMessage;

public class ProductController implements Initializable {
    public Button btnAdd;
    public TextField tfProductId;
    public TextField tfProductName;
    public TextField tfProductPrice;
    public ComboBox cbProductType;
    public TextField tfProductDescription;
    public TextField tfProductOrigin;
    public ComboBox cbUnit;
    public Button btnDelete;
    public Button btnEdit;
    public TableView tvProduct;
    public TextField tfFind;
    public TableColumn tcProductId;
    public TableColumn tcProductName;
    public TableColumn tcProductPrice;
    public TableColumn tcProductDescription;
    public TableColumn tcProductOrigin;
    public TableColumn tcProductUnitSale;
    public TableColumn tcProductUnitImport;
    public TableColumn tcProductType;
    public TableColumn tcProductCoef;

    private Product product;
    public void initData(Product _product) {product=_product;}
    private final ProductDAO productDAO = new ProductDAO();
    private ObservableList<Product> productList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadProductTypes();
        loadUnits();
        loadProducts();
    }

    public void btnAddClicked(MouseEvent mouseEvent) {
        if (checkInformation()) {
            Product newProduct = new Product();
            newProduct.setProductName(tfProductName.getText());
            newProduct.setProductImportPrice((int) Float.parseFloat(tfProductPrice.getText()));
            newProduct.setProductDescription(tfProductDescription.getText());
            newProduct.setProductOrigin(tfProductOrigin.getText());
            newProduct.setProductType((String) cbProductType.getValue());
            newProduct.setProductBigUnit((String) cbUnit.getValue());

            if (productDAO.addProduct(newProduct)) {
                showMessage("Add new product successfully");
                clearInformation();
                loadProducts();
            } else {
                showMessage("Add new product failed");
            }
        } else {
            showMessage("Please fill in all fields");
        }
    }

    public void btnEditClicked(MouseEvent mouseEvent) {
        if (checkInformationEdit()) {
            Product updatedProduct = new Product();
            updatedProduct.setProductId(Integer.parseInt(tfProductId.getText()));
            updatedProduct.setProductName(tfProductName.getText());
            updatedProduct.setProductImportPrice((int) Float.parseFloat(tfProductPrice.getText()));
            updatedProduct.setProductDescription(tfProductDescription.getText());
            updatedProduct.setProductOrigin(tfProductOrigin.getText());
            updatedProduct.setProductType((String) cbProductType.getValue());
            updatedProduct.setProductBigUnit((String) cbUnit.getValue());

            if (productDAO.updateProduct(updatedProduct)) {
                showMessage("Update product successfully");
                clearInformation();
                loadProducts();
            } else {
                showMessage("Update product failed");
            }
        } else {
            showMessage("Please fill in all fields");
        }
    }

    public void btnDeleteClicked(MouseEvent mouseEvent) {
        String productId = tfProductId.getText();
        if (!productId.isEmpty() && productDAO.deleteProduct(productId)) {
            showMessage("Deleted product successfully");
            clearInformation();
            loadProducts();
        } else {
            showMessage("Delete product failed");
        }
    }

    private void loadProductTypes() {
        List<String> productTypes = productDAO.getProductTypes();
        cbProductType.setItems(FXCollections.observableArrayList(productTypes));
    }

    private void loadUnits() {
        List<String> units = productDAO.getUnits();
        cbUnit.setItems(FXCollections.observableArrayList(units));
        cbUnit.getItems().add("Add another unit...");
    }

    private void loadProducts() {
        productList = FXCollections.observableArrayList(productDAO.getAllProduct());
        tvProduct.setItems(productList);
    }

    private boolean checkInformation() {
        return !tfProductName.getText().isEmpty() &&
                !tfProductPrice.getText().isEmpty() &&
                !tfProductDescription.getText().isEmpty() &&
                !tfProductOrigin.getText().isEmpty() &&
                cbUnit.getValue() != null &&
                cbProductType.getValue() != null;
    }

    private boolean checkInformationEdit() {
        return !tfProductId.getText().isEmpty() &&
                !tfProductName.getText().isEmpty() &&
                !tfProductPrice.getText().isEmpty() &&
                !tfProductDescription.getText().isEmpty() &&
                !tfProductOrigin.getText().isEmpty() &&
                cbUnit.getValue() != null &&
                cbProductType.getValue() != null;
    }

    private void clearInformation() {
        tfProductId.clear();
        tfProductName.clear();
        tfProductPrice.clear();
        tfProductDescription.clear();
        tfProductOrigin.clear();
        cbUnit.setValue(null);
        cbProductType.setValue(null);
    }

    private void showMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.show();
    }

}
