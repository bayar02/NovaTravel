package tn.esprit.controllers;

import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import tn.esprit.entities.reclamation;
import tn.esprit.services.ServiceReclamation;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class ReclamationController {

    @FXML
    private TableView<reclamation> tableReclamations;

    @FXML
    private TableColumn<reclamation, String> colType;

    @FXML
    private TableColumn<reclamation, String> colMessage;

    @FXML
    private TableColumn<reclamation, String> colDate;

    @FXML
    private Button btnAjouter;

    @FXML
    private Button btnModifier;
    @FXML
    private TextField  rechercheField;

    @FXML
    private Button btnSupprimer;

    @FXML
    private TextField txtType;

    @FXML
    private TextArea txtMessage;

    private ServiceReclamation serviceReclamation;

    public static int id_reclamation;

    public ReclamationController() {
        serviceReclamation = new ServiceReclamation();
    }

    // Load data into the TableView
    @FXML
    public void initialize() {
        loadReclamations();
        rechercheField.textProperty().addListener((observable, oldValue, newValue) -> {
            rechercherParType();// Appeler la méthode rechercherParNom lorsque le texte dans le champ de recherche change
        });
        tableReclamations.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    // Double-click detected
                    reclamation selectedReclamation = tableReclamations.getSelectionModel().getSelectedItem();
                    if (selectedReclamation != null) {
                        id_reclamation = selectedReclamation.getId();
                        openResponseWindow();
                    }
                }
            }
        });
    }
    private void openResponseWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/addReponse.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(loader.load());

            ReponseController controller = loader.getController();


            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Handle the error
        }
    }

    // Load reclamations from the database and display them in the table
    private void loadReclamations() {
        try {
            List<reclamation> reclamations = serviceReclamation.afficher();
            tableReclamations.getItems().clear();
            tableReclamations.getItems().addAll(reclamations);

            colType.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getType()));
            colMessage.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getMessage()));
            colDate.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDate_reclamation().toString()));

        } catch (SQLException e) {
            showAlert("Error", "Failed to load reclamations", e.getMessage(), AlertType.ERROR);
        }
    }

    // Handle Add action
    @FXML
    private void handleAjouter(MouseEvent event) {
        String type = txtType.getText();
        String message = txtMessage.getText();

        if (type.isEmpty() || message.isEmpty()) {
            showAlert("Validation Error", "Please fill in all fields", "Type and message are required", AlertType.WARNING);
            return;
        }

        try {
            reclamation newReclamation = new reclamation( new java.sql.Date(System.currentTimeMillis()), type, message);
            serviceReclamation.ajouter(newReclamation,serviceReclamation.user.getId());
            loadReclamations();
            clearFields();
            showAlert("Success", "Reclamation added", "The reclamation was added successfully", AlertType.INFORMATION);
        } catch (SQLException e) {
            showAlert("Error", "Failed to add reclamation", e.getMessage(), AlertType.ERROR);
        }
    }

    // Handle Modify action
    @FXML
    private void handleModifier(MouseEvent event) {
        reclamation selectedReclamation = tableReclamations.getSelectionModel().getSelectedItem();
        if (selectedReclamation == null) {
            showAlert("Validation Error", "No reclamation selected", "Please select a reclamation to modify", AlertType.WARNING);
            return;
        }

        String type = txtType.getText();
        String message = txtMessage.getText();

        if (type.isEmpty() || message.isEmpty()) {
            showAlert("Validation Error", "Please fill in all fields", "Type and message are required", AlertType.WARNING);
            return;
        }

        try {
            selectedReclamation.setType(type);
            selectedReclamation.setMessage(message);
            selectedReclamation.setDate_reclamation(new java.sql.Date(System.currentTimeMillis())); // Update date
            serviceReclamation.modifier(selectedReclamation);
            loadReclamations();
            clearFields();
            showAlert("Success", "Reclamation updated", "The reclamation was updated successfully", AlertType.INFORMATION);
        } catch (SQLException e) {
            showAlert("Error", "Failed to update reclamation", e.getMessage(), AlertType.ERROR);
        }
    }

    // Handle Delete action
    @FXML
    private void handleSupprimer(MouseEvent event) {
        reclamation selectedReclamation = tableReclamations.getSelectionModel().getSelectedItem();
        if (selectedReclamation == null) {
            showAlert("Validation Error", "No reclamation selected", "Please select a reclamation to delete", AlertType.WARNING);
            return;
        }

        try {
            serviceReclamation.supprimer(selectedReclamation.getId());
            loadReclamations();
            clearFields();
            showAlert("Success", "Reclamation deleted", "The reclamation was deleted successfully", AlertType.INFORMATION);
        } catch (SQLException e) {
            showAlert("Error", "Failed to delete reclamation", e.getMessage(), AlertType.ERROR);
        }
    }

    // Clear input fields
    private void clearFields() {
        txtType.clear();
        txtMessage.clear();
    }

    // Show alert dialog
    private void showAlert(String title, String header, String content, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
    @FXML
    private void rechercherParType() {
        String typeRecherche = rechercheField.getText().trim().toLowerCase();
        if (!typeRecherche.isEmpty()) {
            List<reclamation> filteredReclamations = tableReclamations.getItems()
                    .stream()
                    .filter(reclamation -> reclamation.getType().toLowerCase().contains(typeRecherche))
                    .collect(Collectors.toList());
            tableReclamations.setItems(FXCollections.observableArrayList(filteredReclamations));
        } else {
            loadReclamations();
        }
    }
}
