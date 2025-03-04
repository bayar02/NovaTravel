package Controllers;

import Entities.Reclamation;
import Services.ReclamationService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;

public class AddReclamationController implements Initializable {

    @FXML
    private Button cancel;

    @FXML
    private DatePicker dateReclamation;

    @FXML
    private Label errorLabel;

    @FXML
    private TextArea messageReclamation;

    @FXML
    private Button save;

    @FXML
    private ComboBox<String> typeReclamation;

    private final ReclamationService reclamationService = new ReclamationService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        typeReclamation.getItems().addAll(
                "Booking Issues",
                "Transportation Problems",
                "Payment & Refund Issues",
                "Safety & Security Concerns"
        );
    }

    @FXML
    void handleCancel(ActionEvent event) {
        // Close the window
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }

    @FXML
    void handleSave(ActionEvent event) {
        // Input validation
        if (dateReclamation.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez sélectionner une date de réclamation.");
            return;
        }

        if (typeReclamation.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez choisir un type de réclamation.");
            return;
        }

        String message = messageReclamation.getText().trim();
        if (message.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le message de la réclamation ne peut pas être vide.");
            return;
        }

        if (message.length() < 10) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le message doit contenir au moins 10 caractères.");
            return;
        }

        // Create reclamation object
        Reclamation reclamation = new Reclamation(
                1,  // Static user ID
                java.sql.Date.valueOf(dateReclamation.getValue()),
                typeReclamation.getValue(),
                message
        );

        // Save to database
        try {
            reclamationService.ajouter(reclamation);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Réclamation ajoutée avec succès!");

            // Close the window after success
            Stage stage = (Stage) save.getScene().getWindow();
            stage.close();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de l'ajout de la réclamation.");
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
