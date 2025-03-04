package Controllers;

import Entities.Reclamation;
import Entities.Reponse;
import Services.ReclamationService;
import Services.ReponseService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class UpdateReponseController implements Initializable {

    @FXML
    private ComboBox<Reclamation> ReclamationM;

    @FXML
    private Button cancel;

    @FXML
    private DatePicker dateReponse;

    @FXML
    private Label errorLabel;

    @FXML
    private TextArea messageReponse;

    @FXML
    private Button save;

    private final ReponseService reponseService = new ReponseService();
    private final ReclamationService reclamationService = new ReclamationService();

    private Reponse selectedReponse; // The response to be updated



    @FXML
    void handleCancel(ActionEvent event) {
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }

    @FXML
    void handleSave(ActionEvent event) {
        // Validate input fields
        if (ReclamationM.getValue() == null || dateReponse.getValue() == null || messageReponse.getText().isEmpty()) {
            errorLabel.setText("⚠ Please fill in all fields!");
            return;
        }

        try {
            // Update response object
            selectedReponse.setIdReclamation(ReclamationM.getValue().getId());
            selectedReponse.setMessage(messageReponse.getText());
            selectedReponse.setDateReponse(Date.valueOf(dateReponse.getValue()));

            // Update in database
            reponseService.modifier(selectedReponse);
            System.out.println("✅ Response updated successfully!");

            // Show success alert
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("✅ Response updated successfully!");
            alert.showAndWait();

            // Close window after update
            handleCancel(event);

        } catch (SQLException e) {
            errorLabel.setText("❌ Error updating response: " + e.getMessage());
            System.err.println("Error: " + e.getMessage());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadReclamations();
    }

    public void setReponses(Reponse reponse) {
        this.selectedReponse = reponse;
        messageReponse.setText(reponse.getMessage());
        dateReponse.setValue(((java.sql.Date) reponse.getDateReponse()).toLocalDate());

        // Load reclamations
        loadReclamations();

        // Find the corresponding reclamation
        for (Reclamation r : ReclamationM.getItems()) {
            if (r.getId() == reponse.getIdReclamation()) {
                ReclamationM.getSelectionModel().select(r);
                break;
            }
        }
    }

    private void loadReclamations() {
        try {
            List<Reclamation> reclamations = reclamationService.afficher();
            ObservableList<Reclamation> reclamationList = FXCollections.observableArrayList(reclamations);

            // Display only the message in the ComboBox
            ReclamationM.setCellFactory(lv -> new ListCell<>() {
                @Override
                protected void updateItem(Reclamation reclamation, boolean empty) {
                    super.updateItem(reclamation, empty);
                    setText(empty ? null : reclamation.getMessage());
                }
            });

            ReclamationM.setButtonCell(new ListCell<>() {
                @Override
                protected void updateItem(Reclamation reclamation, boolean empty) {
                    super.updateItem(reclamation, empty);
                    setText(empty ? null : reclamation.getMessage());
                }
            });

            ReclamationM.setItems(reclamationList);
        } catch (SQLException e) {
            System.err.println("❌ Error loading reclamations: " + e.getMessage());
        }
    }


}
