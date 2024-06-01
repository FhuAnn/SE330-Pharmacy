package com.example.se330_pharmacy.Controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import com.example.se330_pharmacy.DataAccessObject.ImportDAO;
import com.example.se330_pharmacy.Models.*;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import javax.swing.*;
import java.net.URL;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static com.almasb.fxgl.dsl.FXGLForKtKt.showMessage;

public class ImportController implements Initializable {
    public TableView<Product> tbl_ProductTable;
    public TableColumn<Product,Integer> col_idProduct;
    public TableColumn<Product,String> col_nameProduct;
    public TableColumn<Product,Integer> col_priceProduct;
    public TableColumn<Product,String> col_descriptionProduct;
    public TableColumn<Product,String> col_originProduct;
    public TableColumn<Product,String> col_unitProduct;
    public TableColumn<Product,String> col_typeProduct;
    public TableView<DetailImport> tbl_ImportForm;
    public TableColumn<DetailImport,Integer> col_idDetail;
    public TableColumn<DetailImport,String> col_nameDetail;
    public TableColumn<DetailImport,Integer> col_priceDetail;
    public TableColumn<DetailImport,String> col_quantityDetail;
    public TableColumn<DetailImport,String> col_totalDetail;
    public TableView<Supplier> tbl_Supplier;
    public TableColumn<Supplier,String> col_namePartner;
    public TableColumn<Product,String> col_address;
    public TableColumn<Product,Integer> col_phonenumberPartner;
    public TextField tfProductID;
    public TextField tfProductName;
    public TextField tfProductPrice;
    public TextField tfProductQuantities;
    public TextField tfProductTotal;
    public TextField tf_supplier;
    public Button btnAdd;
    public Button btnEdit;
    public Button btnCancel;
    public Button btnCreateForm;
    public Button btnDelete;
    public Button btnShow;
    public TextField tfFind;
    public Pane panelResultSupplier;
    public Text lblTotalPay;
    String message;
    private final ImportDAO importDAO = new ImportDAO();
    private ObservableList<Product> ListProducts;
    private ObservableList<Supplier> ListSuppliers;
    Employee employee_init;
    Supplier supplierBeforeClicked;
    public void InitData(Employee _employee){
        employee_init = _employee;
    }
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SetUp();
        loadProducts();
        loadSupplier();
        AddListenner();
    }

    private void SetUp() {
        panelResultSupplier.setVisible(false);

        col_idProduct.setCellValueFactory(new PropertyValueFactory<>("productId"));
        col_nameProduct.setCellValueFactory(new PropertyValueFactory<>("productName"));
        col_priceProduct.setCellValueFactory(new PropertyValueFactory<>("productImportPrice"));
        col_descriptionProduct.setCellValueFactory(new PropertyValueFactory<>("productDescription"));
        col_originProduct.setCellValueFactory(new PropertyValueFactory<>("productOrigin"));
        col_unitProduct.setCellValueFactory(new PropertyValueFactory<>("productBigUnit"));
        col_typeProduct.setCellValueFactory(new PropertyValueFactory<>("productType"));

        col_idDetail.setCellValueFactory(new PropertyValueFactory<>("productId"));
        col_nameDetail.setCellValueFactory(new PropertyValueFactory<>("productName"));
        col_priceDetail.setCellValueFactory(new PropertyValueFactory<>("importPrice"));
        col_quantityDetail.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        col_totalDetail.setCellValueFactory(new PropertyValueFactory<>("total"));

        col_namePartner.setCellValueFactory(new PropertyValueFactory<>("partnername"));
        col_address.setCellValueFactory(new PropertyValueFactory<>("address"));
        col_phonenumberPartner.setCellValueFactory(new PropertyValueFactory<>("phonenumber"));
    }


    private void AddListenner() {
        tbl_ProductTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount()==2 ) {
                    if(!tbl_Supplier.getItems().isEmpty()&&CheckNotExistProductInImportTable(tbl_ProductTable.getSelectionModel().getSelectedItem().getProductId()))
                    {
                        FillProductTableToTextField();
                    } else showAlert("Warning",message);
                }
            }
        });
        tfFind.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!newValue.isEmpty()){
                    SearchProduct(newValue);
                } else {
                    tbl_ProductTable.setItems(ListProducts);
                }
            }
        });
        tbl_ImportForm.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount()==2) {
                    FillImportTableToTextField();
                }
            }
        });
        tbl_Supplier.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount()==1) {
                    supplierBeforeClicked= tbl_Supplier.getSelectionModel().getSelectedItem();
                    tf_supplier.setText(supplierBeforeClicked.getPartnername());
                }
            }
        });
        tf_supplier.textProperty().addListener((observable,oldValue, newValue) -> {
            if(!newValue.isEmpty()) {
                SearchSupplier(newValue);
            } else tbl_Supplier.setItems(ListSuppliers);
        });
        tfProductQuantities.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!newValue.isEmpty()) {
                    if (!newValue.matches("\\d*")) {
                        tfProductQuantities.setText(newValue.replaceAll("[^\\d]", ""));
                        showAlert("Warning","Chỉ nhập được nhập số!");
                    } else {
                        int total = Integer.parseInt(newValue)* Integer.parseInt(tfProductPrice.getText());
                        tfProductTotal.setText(String.valueOf(total));
                    }
                }
            }
        });
    }

    private boolean CheckNotExistProductInImportTable(int id) {
        ObservableList<DetailImport> listproducts = tbl_ImportForm.getItems();
        for(DetailImport detailImport : listproducts) {
            if(detailImport.getProduct_id() == id)  {
                message="Bạn thêm sản phẩm này vào danh sách!";
                return false;
            }
        }
        return true;
    }

    private void SearchSupplier(String searchString) {
        String lowerCase = normalizeString(searchString.toLowerCase());
        ObservableList<Supplier> users = tbl_Supplier.getItems();
        ObservableList<Supplier> listResult = FXCollections.observableArrayList(
                users.stream()
                        .filter(supplier ->
                                normalizeString(String.valueOf(supplier.getPartner_id()).toLowerCase()).startsWith(lowerCase) ||
                                        normalizeString(supplier.getPartnername().toLowerCase()).contains(lowerCase))
                        .collect(Collectors.toList())
        );
        tbl_Supplier.setItems(listResult);
    }
    private void SearchProduct(String searchString) {
        String lowerCase = normalizeString(searchString.toLowerCase());
        ObservableList<Product> users = tbl_ProductTable.getItems();
        ObservableList<Product> listResult = FXCollections.observableArrayList(
                users.stream()
                        .filter(product ->
                                normalizeString(String.valueOf(product.getProductId()).toLowerCase()).startsWith(lowerCase) ||
                                        normalizeString(product.getProductName().toLowerCase()).contains(lowerCase))
                        .collect(Collectors.toList())
        );
        tbl_ProductTable.setItems(listResult);
    }
    private void FillImportTableToTextField() {
        tfProductQuantities.setDisable(true);
        DetailImport importProduct = tbl_ImportForm.getSelectionModel().getSelectedItem();
        tfProductID.setText(String.valueOf(importProduct.getProduct_id()));
        tfProductName.setText(importProduct.getProductName());
        tfProductPrice.setText(String.valueOf(importProduct.getImportPrice()));
        tfProductQuantities.setText(String.valueOf(importProduct.getQuantity()));
        tfProductTotal.setText(String.valueOf(importProduct.getTotal()));
    }
    private void FillProductTableToTextField() {
        tfProductQuantities.setDisable(false);
        btnEdit.setText("Sửa");
        btnEdit.setId("edit");
        Product product = tbl_ProductTable.getSelectionModel().getSelectedItem();
        tfProductID.setText(String.valueOf(product.getProductId()));
        tfProductName.setText(product.getProductName());
        tfProductPrice.setText(String.valueOf(product.getProductImportPrice()));
    }


    public void btnEditClicked(MouseEvent mouseEvent) {
        if(!tfProductID.getText().isEmpty()) {
            if(btnEdit.getText().equals("Sửa")) {
                tfProductQuantities.setDisable(false);
                btnEdit.setId("add");
                btnEdit.setText("Lưu");
            } else {
                int sequence = ShowYesNoAlert("lưu");
                if(sequence==JOptionPane.YES_OPTION) {
                    UpdateImportTable();
                    LoadTotalPayFinal();
                    tfProductQuantities.setDisable(true);
                    btnEdit.setId("edit");
                    btnEdit.setText("Sửa");
                } else {
                }
            }
        }

       /* if(!tfProductID.getText().isEmpty()) {
            SetDisable(false);
        }
        DetailImport selectedDetailImport = (DetailImport) tbl_ProductTable.getSelectionModel().getSelectedItem();
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

        // 3. Khi người dùng thực hiện chỉnh sửa và nhấn nút "Edit"
        if (checkInformation()) {
            // Tạo một đối tượng DetailImport mới từ dữ liệu nhập vào
            DetailImport updatedDetailImport = new DetailImport(
                    selectedDetailImport.getImportId(),
                    tfProductID.getText(),
                    tfProductName.getText(),
                    Float.parseFloat(tfProductPrice.getText()),
                    Integer.parseInt(tfProductQuantities.getText()),
                    Double.parseDouble(tfProductTotal.getText());
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
*/
    }

    private void LoadTotalPayFinal() {
        ObservableList<DetailImport> items = tbl_ImportForm.getItems();
        int total = 0;
        for(DetailImport detailImport : items) {
            total+= detailImport.getTotal();
        }
        lblTotalPay.setText(total+" VND");
    }

    private void UpdateImportTable() {
        int delete_pos= DeleteRowInImportTable(tbl_ImportForm.getItems(),tbl_ImportForm.getSelectionModel().getSelectedItem().getProduct_id());
        DetailImport detailImport = new DetailImport(
                Integer.parseInt(tfProductID.getText()),
                tfProductName.getText(),Integer.parseInt(tfProductPrice.getText()),
                Integer.parseInt(tfProductQuantities.getText()),
                Integer.parseInt(tfProductTotal.getText()));
        tbl_ImportForm.getItems().add(delete_pos,detailImport);
    }

    private int DeleteRowInImportTable(ObservableList<DetailImport> items, int productId) {
        int index =0,pos_delete=0;
        for(DetailImport detailImport : items)
        {
            if(detailImport.getProduct_id()==productId) {
                pos_delete=index;
                break;
            }
            index++;
        }
        items.remove(pos_delete);
        return pos_delete;
    }

    public void btnAddClicked(MouseEvent mouseEvent) {
        String id = tfProductID.getText();
        String productname = tfProductName.getText();
        String price = tfProductPrice.getText();
        String quantity = tfProductQuantities.getText();
        String total = tfProductTotal.getText();
        if(!id.isEmpty() && !productname.isEmpty() && !price.isEmpty() && !quantity.isEmpty() && !total.isEmpty()) {
            DetailImport detailImport_ = new DetailImport(Integer.parseInt(id),productname,Integer.parseInt(price),Integer.parseInt(quantity),Integer.parseInt(total));
            tbl_ImportForm.getItems().add(detailImport_);
            LoadTotalPayFinal();
            clearInformation();
        } else {
            showAlert("Warning","Kiểm tra đảm bảo đầy đủ thông tin!");
        }
    }

    public void btnCancelClicked(MouseEvent mouseEvent) {
        int sequence = ShowYesNoAlert("huỷ");
        if(sequence==JOptionPane.YES_OPTION) {
            clearInformation();
            tfProductQuantities.setDisable(false);
            tbl_ImportForm.getItems().clear();
        }
    }

    public void btnCreateFormClicked(MouseEvent mouseEvent) {
        if(!tbl_ImportForm.getItems().isEmpty()) {
            if(tf_supplier.getText().isEmpty()) {
                showAlert("Warning","Bạn chưa chọn nhà cung cấp!");
                return;
            }
            String string = lblTotalPay.getText();
            String[] splits =string.split(" ");
            int totalPay = Integer.parseInt(splits[0]);
            Import import_ = new Import(
                    employee_init.getEmployeeId(),
                    supplierBeforeClicked.getPartner_id(),
                    totalPay,
                    LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            if(importDAO.addImportData(import_)>0) {
                showAlert("Warning","Thông tin nhập hàng được ghi nhận!");
                clearInformation();
                tbl_ImportForm.getItems().clear();
            }
        }
    }

    public void clearInformation() {
        tfProductID.clear();
        tfProductName.clear();
        tfProductPrice.clear();
        tfProductQuantities.clear();
        tfProductTotal.clear();
        tf_supplier.clear();
    }
    private void loadProducts() {
        ListProducts = importDAO.getProductData();
        tbl_ProductTable.setItems(ListProducts);
    }
    private void loadSupplier() {
        ListSuppliers= importDAO.getSuppliers();
        tbl_Supplier.setItems(ListSuppliers);
    }
    public void handleButtonShow(ActionEvent event) {
        if(panelResultSupplier.isVisible()) {
            panelResultSupplier.setVisible(false);
            btnShow.setId("orange");
            btnShow.setText("Xem");
        } else {
            tbl_Supplier.setItems(ListSuppliers);
            tf_supplier.promptTextProperty().setValue("Nhập id hoặc tên nhà cung cấp!");
            panelResultSupplier.setVisible(true);
            btnShow.setId("delete");
            btnShow.setText("X");
        }
    }
    public void btnDeleteClicked(MouseEvent mouseEvent) {
        if (tbl_ImportForm.getSelectionModel().getSelectedItem() != null) {
        }
    }


    private static String normalizeString(String str) {
        return Normalizer.normalize(str, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }
    private void showAlert(String tilte,String string) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(tilte);
        alert.setHeaderText(null);
        alert.setContentText(string);
        alert.showAndWait();
    }

    private int ShowYesNoAlert(String string) {
        JFrame frame = new JFrame("Xác nhận");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        return JOptionPane.showConfirmDialog(frame, "Bạn muốn "+string+"?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
    }
}
