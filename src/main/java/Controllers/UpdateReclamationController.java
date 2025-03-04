package Controllers;

import Entities.Reclamation;
import Services.ReclamationService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.sql.Date; // Import this
import java.time.ZoneId;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UpdateReclamationController implements Initializable {

    @FXML
    private DatePicker dateReclamation;

    @FXML
    private ComboBox<String> typeReclamation;

    @FXML
    private TextArea messageReclamation;

    @FXML
    private Button save;

    @FXML
    private Button cancel;

    private Reclamation reclamation;
    private ReclamationService reclamationService = new ReclamationService();

    public void setReclamation(Reclamation reclamation) {
        this.reclamation = reclamation;
        if (reclamation != null) {
            if (reclamation.getDateReclamation() != null) { // Ensure date is not null
                LocalDate localDate = ((java.sql.Date) reclamation.getDateReclamation()).toLocalDate();
                dateReclamation.setValue(localDate);
            }
            typeReclamation.setValue(reclamation.getType());
            messageReclamation.setText(reclamation.getMessage());
        }
    }


    @FXML
    void handleSave(ActionEvent event) {
        try {
            if (dateReclamation.getValue() != null) {
                reclamation.setDateReclamation(java.sql.Date.valueOf(dateReclamation.getValue())); // ✅ Fix conversion
            }

            reclamation.setType(typeReclamation.getValue());
            reclamation.setMessage(messageReclamation.getText());

            reclamationService.modifier(reclamation);

            Stage stage = (Stage) save.getScene().getWindow();
            stage.close();

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de mettre à jour la réclamation.");
        }
    }


    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void handleCancel(ActionEvent event) {
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // ✅ Populate typeReclamation choices
        typeReclamation.getItems().addAll(
                "Booking Issues",
                "Transportation Problems",
                "Payment & Refund Issues",
                "Safety & Security Concerns"
        );
    }
}
