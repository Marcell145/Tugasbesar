package com.example.demo_uas;

import javafx.application.Application;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class PinjamanHpApp extends Application {

    private ObservableList<PinjamanHp> pinjamanHpList = FXCollections.observableArrayList();
    private TableView<PinjamanHp> tableView = new TableView<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Pinjaman HP Application");

        // Form components
        TextField brandField = createStyledTextField();
        TextField modelField = createStyledTextField();
        TextField jumlahPinjamanField = createStyledTextField();
        TextField hargaField = createStyledTextField();
        Button createButton = new Button("Create");
        Button clearButton = new Button("Clear");
        Button updateButton = new Button("Update");
        Button deleteButton = new Button("Delete");

        // Table columns
        TableColumn<PinjamanHp, String> brandColumn = new TableColumn<>("Brand");
        brandColumn.setCellValueFactory(cellData -> cellData.getValue().brandProperty());

        TableColumn<PinjamanHp, String> modelColumn = new TableColumn<>("Model");
        modelColumn.setCellValueFactory(cellData -> cellData.getValue().modelProperty());

        TableColumn<PinjamanHp, String> jumlahPinjamanColumn = new TableColumn<>("Jumlah Pinjaman");
        jumlahPinjamanColumn.setCellValueFactory(cellData -> cellData.getValue().jumlahPinjamanProperty());

        TableColumn<PinjamanHp, Double> hargaColumn = new TableColumn<>("Harga");
        hargaColumn.setCellValueFactory(cellData -> cellData.getValue().hargaProperty().asObject());

        // Add columns to table
        tableView.getColumns().addAll(brandColumn, modelColumn, jumlahPinjamanColumn, hargaColumn);
        tableView.setItems(pinjamanHpList);

        // Button actions
        createButton.setOnAction(event -> {
            String brand = brandField.getText();
            String model = modelField.getText();
            String jumlahPinjaman = jumlahPinjamanField.getText();
            double harga = 0.0;

            try {
                if (!isValidNumber(jumlahPinjaman) || !isValidNumber(hargaField.getText())) {
                    throw new NumberFormatException("Jumlah Pinjaman dan Harga harus berupa angka.");
                }

                harga = Double.parseDouble(hargaField.getText());
            } catch (NumberFormatException e) {
                showAlert("Format jumlah pinjaman atau harga tidak valid! Harus berupa angka.");
                return;
            }

            if (!brand.isEmpty() && !model.isEmpty()) {
                PinjamanHp newLoan = new PinjamanHp(brand, model, jumlahPinjaman, harga);
                pinjamanHpList.add(newLoan);
                clearFields(brandField, modelField, jumlahPinjamanField, hargaField);
            } else {
                showAlert("Semua kolom harus diisi!");
            }
        });

        clearButton.setOnAction(event -> {
            clearFields(brandField, modelField, jumlahPinjamanField, hargaField);
        });

        updateButton.setOnAction(event -> {
            PinjamanHp selectedLoan = tableView.getSelectionModel().getSelectedItem();
            if (selectedLoan != null) {
                String updatedBrand = brandField.getText();
                String updatedModel = modelField.getText();
                String updatedJumlahPinjaman = jumlahPinjamanField.getText();
                String updatedHargaText = hargaField.getText();

                // Cek apakah TextField tidak kosong sebelum melakukan pembaruan
                if (!updatedBrand.isEmpty()) {
                    selectedLoan.setBrand(updatedBrand);
                }

                if (!updatedModel.isEmpty()) {
                    selectedLoan.setModel(updatedModel);
                }

                try {
                    if (!updatedJumlahPinjaman.isEmpty() && isValidNumber(updatedJumlahPinjaman)) {
                        selectedLoan.setJumlahPinjaman(updatedJumlahPinjaman);
                    }

                    if (!updatedHargaText.isEmpty() && isValidNumber(updatedHargaText)) {
                        double updatedHarga = Double.parseDouble(updatedHargaText);
                        selectedLoan.setHarga(updatedHarga);
                    }
                } catch (NumberFormatException e) {
                    showAlert("Format jumlah pinjaman atau harga tidak valid! Harus berupa angka.");
                    return;
                }

                tableView.refresh();
                clearFields(brandField, modelField, jumlahPinjamanField, hargaField);
            } else {
                showAlert("Pilih pinjaman untuk diupdate!");
            }
        });

        deleteButton.setOnAction(event -> {
            int selectedIndex = tableView.getSelectionModel().getSelectedIndex();
            if (selectedIndex != -1) {
                tableView.getItems().remove(selectedIndex);
                clearFields(brandField, modelField, jumlahPinjamanField, hargaField);
            } else {
                showAlert("Pilih pinjaman untuk dihapus!");
            }
        });

        // Layout
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);

        grid.add(new Label("Brand:"), 0, 0);
        grid.add(brandField, 1, 0);
        grid.add(new Label("Model:"), 0, 1);
        grid.add(modelField, 1, 1);
        grid.add(new Label("Jumlah Pinjaman:"), 0, 2);
        grid.add(jumlahPinjamanField, 1, 2);
        grid.add(new Label("Harga:"), 0, 3);
        grid.add(hargaField, 1, 3);
        grid.add(createButton, 0, 4);
        grid.add(clearButton, 1, 4);
        grid.add(updateButton, 0, 5);
        grid.add(deleteButton, 1, 5);
        grid.add(tableView, 0, 6, 2, 1);

        // Lebar kolom diperluas
        brandColumn.setMinWidth(100);
        modelColumn.setMinWidth(100);
        jumlahPinjamanColumn.setMinWidth(150);
        hargaColumn.setMinWidth(100);

        Scene scene = new Scene(grid, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields(TextField... fields) {
        for (TextField field : fields) {
            field.clear();
        }
    }

    private boolean isValidNumber(String input) {
        return input.matches("\\d*\\.?\\d+");
    }

    private TextField createStyledTextField() {
        TextField textField = new TextField();
        textField.setStyle("-fx-control-inner-background: lightblue;");
        return textField;
    }

    public static class PinjamanHp {
        private final StringProperty brand;
        private final StringProperty model;
        private final StringProperty jumlahPinjaman;
        private final DoubleProperty harga;

        public PinjamanHp(String brand, String model, String jumlahPinjaman, double harga) {
            this.brand = new SimpleStringProperty(brand);
            this.model = new SimpleStringProperty(model);
            this.jumlahPinjaman = new SimpleStringProperty(jumlahPinjaman);
            this.harga = new SimpleDoubleProperty(harga);
        }

        public String getBrand() {
            return brand.get();
        }

        public void setBrand(String brand) {
            this.brand.set(brand);
        }

        public StringProperty brandProperty() {
            return brand;
        }

        public String getModel() {
            return model.get();
        }

        public void setModel(String model) {
            this.model.set(model);
        }

        public StringProperty modelProperty() {
            return model;
        }

        public String getJumlahPinjaman() {
            return jumlahPinjaman.get();
        }

        public void setJumlahPinjaman(String jumlahPinjaman) {
            this.jumlahPinjaman.set(jumlahPinjaman);
        }

        public StringProperty jumlahPinjamanProperty() {
            return jumlahPinjaman;
        }

        public double getHarga() {
            return harga.get();
        }

        public void setHarga(double harga) {
            this.harga.set(harga);
        }

        public DoubleProperty hargaProperty() {
            return harga;
        }
    }
}
