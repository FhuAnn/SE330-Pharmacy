package com.example.se330_pharmacy.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import com.example.se330_pharmacy.DataAccessObject.ImportDAO;
import com.example.se330_pharmacy.Models.*;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import static com.almasb.fxgl.dsl.FXGLForKtKt.showMessage;

public class ImportController {
    public TableView tvImportForm;
    public TextField tfProductID;
    public TextField tfProductName;
    public TextField tfProductPrice;
    public TextField tfProductQuantities;
    public TextField tfProductTotal;
    public ComboBox cbbSupplier;
    public TableView tvImportProductTable;
    public Button btnAdd;
    public Button btnEdit;
    public Button btnCancel;
    public Button btnCreateForm;
    public Button btnDelete;
    public TextField tfFind;

    private final ImportDAO productDAO = new ImportDAO();
    private ObservableList<DetailImport> importProductList;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadImportProducts();
        loadImportForm();
    }

    

    public void btnEditClicked(MouseEvent mouseEvent) {
        DetailImport selectedDetailImport = (DetailImport) tvImportProductTable.getSelectionModel().getSelectedItem();
        if (selectedDetailImport == null) {
            showMessage("Please select a product to edit");
            return;
        }

        // 2. Hiển thị dữ liệu của sản phẩm được chọn lên các trường nhập liệu
        tfProductID.setText(selectedDetailImport.getProduct_id());
        tfProductName.setText(selectedDetailImport.getProductName());
        tfProductPrice.setText(String.valueOf(selectedDetailImport.getImportPrice()));
        tfProductQuantities.setText(String.valueOf(selectedDetailImport.getQuantity()));
        tfProductTotal.setText(String.valueOf(selectedDetailImport.getTotal()));
        cbbSupplier.setValue(selectedDetailImport.getSupplier());

        // 3. Khi người dùng thực hiện chỉnh sửa và nhấn nút "Edit"
        if (checkInformation()) {
            // Tạo một đối tượng DetailImport mới từ dữ liệu nhập vào
            DetailImport updatedDetailImport = new DetailImport(
                    selectedDetailImport.getImportId(),
                    tfProductID.getText(),
                    tfProductName.getText(),
                    Float.parseFloat(tfProductPrice.getText()),
                    Integer.parseInt(tfProductQuantities.getText()),
                    Double.parseDouble(tfProductTotal.getText()),
                    cbbSupplier.getValue()
            );
            // Cập nhật dữ liệu của sản phẩm trong cơ sở dữ liệu
            if (ImportDAO.updateImportDetail(updatedDetailImport)) {
                showMessage("Update detail import successfully");
                clearInformation(); // Xóa thông tin sau khi chỉnh sửa thành công
                loadImportProducts(); // Cập nhật lại bảng sản phẩm nhập
            } else {
                showMessage("Update detail import failed");
            }
        } else {
            showMessage("Please fill in all fields");
        }

    }

    public void btnAddClicked(MouseEvent mouseEvent) {
            // 1. Lấy dữ liệu từ các trường nhập liệu
            String productId = tfProductID.getText();
            String productName = tfProductName.getText();
            String productPrice = tfProductPrice.getText();
            String productQuantities = tfProductQuantities.getText();
            String productTotal = tfProductTotal.getText();
            String supplier = (String) cbbSupplier.getValue();

            // 2. Kiểm tra xem các trường đã được nhập đầy đủ chưa
            if (productId.isEmpty() || productName.isEmpty() || productPrice.isEmpty() ||
                    productQuantities.isEmpty() || productTotal.isEmpty() || supplier == null) {
                showMessage("Please fill in all fields");
                return;
            }

            // 3. Tạo một đối tượng DetailImport mới
            DetailImport newDetailImport = new DetailImport(productId, productName, Float.parseFloat(productPrice),
                    Integer.parseInt(productQuantities), Double.parseDouble(productTotal), supplier);

            // 4. Gọi phương thức thêm dữ liệu từ ImportDAO
            if (ImportDAO.addImportDetail(newDetailImport)) {
                showMessage("Add detail import successfully");
                clearInformation(); // Xóa thông tin sau khi thêm thành công
                // Cập nhật lại bảng sản phẩm nhập
                loadImportProducts();
            } else {
                showMessage("Add detail import failed");
            }
    }

    public void btnCancelClicked(MouseEvent mouseEvent) {
        clearInformation(); // Xóa thông tin đã nhập trong các trường nhập liệu
        tvImportProductTable.getSelectionModel().clearSelection(); // Xóa lựa chọn trong bảng sản phẩm nhập
    }

    public void btnCreateFormClicked(MouseEvent mouseEvent) {
        if (validateForm()) {
            // Lấy thông tin từ các trường nhập liệu
            String productId = tfProductID.getText();
            String productName = tfProductName.getText();
            float productPrice = Float.parseFloat(tfProductPrice.getText());
            int productQuantities = Integer.parseInt(tfProductQuantities.getText());
            float productTotal = Float.parseFloat(tfProductTotal.getText());
            String supplier = cbbSupplier.getValue().toString();

            // Thêm dữ liệu vào cơ sở dữ liệu thông qua đối tượng ImportDAO
            if (productDAO.addImportData(productId, productName, productPrice, productQuantities, productTotal, supplier)) {
                showMessage("Created import form successfully. Do you want to print this form?");
                clearForm(); // Xóa thông tin đã nhập
                loadImportForm(); // Tải lại dữ liệu biểu mẫu
            } else {
                showMessage("Failed to create import form.");
            }
        } else {
            showMessage("Please fill in all fields.");
        }
    }

    private void loadSuppliers() {
        ObservableList supplierList = FXCollections.observableArrayList(ImportDAO.getSuppliers());
        cbbSupplier.setItems(supplierList);
    }
    public void clearInformation() {
        tfProductID.clear();
        tfProductName.clear();
        tfProductPrice.clear();
        tfProductQuantities.clear();
        tfProductTotal.clear();
        cbbSupplier.setValue(null);
        tvImportProductTable.getItems().clear();
    }
    private void loadImportProducts() {
        List<DetailImport> importProducts = ImportDAO.getAllImportProducts();
        importProductList = FXCollections.observableArrayList(importProducts);
        tvImportProductTable.setItems(importProductList);
    }

    private boolean checkInformation() {
        // Check if any of the required fields are empty
        if (tfProductID.getText().isEmpty() ||
                tfProductName.getText().isEmpty() ||
                tfProductPrice.getText().isEmpty() ||
                tfProductQuantities.getText().isEmpty() ||
                tfProductTotal.getText().isEmpty() ||
                cbbSupplier.getValue() == null) {
            // If any required field is empty, return false
            return false;
        }

        // Check if the price, quantities, and total are valid numbers
        try {
            float price = Float.parseFloat(tfProductPrice.getText());
            int quantities = Integer.parseInt(tfProductQuantities.getText());
            double total = Double.parseDouble(tfProductTotal.getText());

            // You can also add additional validation rules here if needed
            // For example, ensure that price, quantities, and total are positive numbers
            if (price <= 0 || quantities <= 0 || total <= 0) {
                return false;
            }
        } catch (NumberFormatException e) {
            // If price, quantities, or total cannot be parsed as valid numbers, return false
            return false;
        }

        // If all checks pass, return true
        return true;
    }

    private boolean validateForm() {
        // Check if any of the required fields are empty
        if (tfProductID.getText().isEmpty() ||
                tfProductName.getText().isEmpty() ||
                tfProductPrice.getText().isEmpty() ||
                tfProductQuantities.getText().isEmpty() ||
                tfProductTotal.getText().isEmpty() ||
                cbbSupplier.getValue() == null) {
            // If any required field is empty, show an error message and return false
            showMessage("Please fill in all fields.");
            return false;
        }

        // Check if the price, quantities, and total are valid numbers
        try {
            float price = Float.parseFloat(tfProductPrice.getText());
            int quantities = Integer.parseInt(tfProductQuantities.getText());
            double total = Double.parseDouble(tfProductTotal.getText());

            // Check if the parsed numbers are positive
            if (price <= 0 || quantities <= 0 || total <= 0) {
                // If any of them are not positive, show an error message and return false
                showMessage("Price, quantities, and total must be positive numbers.");
                return false;
            }
        } catch (NumberFormatException e) {
            // If price, quantities, or total cannot be parsed as valid numbers, show an error message and return false
            showMessage("Price, quantities, and total must be valid numbers.");
            return false;
        }

        // If all checks pass, return true
        return true;
    }

    // Inside ImportController
    public void clearForm() {
        tfProductID.setText("");
        tfProductName.setText("");
        tfProductPrice.setText("");
        tfProductQuantities.setText("");
        tfProductTotal.setText("");
        cbbSupplier.getSelectionModel().clearSelection();
    }

    public void loadImportForm() {

        ImportDAO importDAO = new ImportDAO();
        ObservableList<Product> importedProducts = FXCollections.observableArrayList();
        tvImportProductTable.setItems(importedProducts);

    }


    public void btnDeleteClicked(MouseEvent mouseEvent) {
        if (tvImportProductTable.getSelectionModel().getSelectedItem() != null) {
            // Lấy thông tin sản phẩm được chọn
            Product selectedProduct = (Product) tvImportProductTable.getSelectionModel().getSelectedItem();
            String productId = String.valueOf(selectedProduct.getProductId());

            // Xác nhận xóa
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to delete this product?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Gọi phương thức xóa sản phẩm từ đối tượng ImportDAO
                if (productDAO.deleteProduct(productId)) {
                    showMessage("Deleted product successfully");
                    loadImportForm(); // Tải lại dữ liệu biểu mẫu sau khi xóa
                } else {
                    showMessage("Failed to delete product");
                }
            }
        } else {
            showMessage("Please select a product to delete");
        }
    }
}
