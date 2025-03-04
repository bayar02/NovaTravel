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

public class AddReponseController implements Initializable {

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
            // Create a new response object
            Reponse reponse = new Reponse(
                    ReclamationM.getValue().getId(),  // Get selected reclamation ID
                    messageReponse.getText(),
                    Date.valueOf(dateReponse.getValue())  // Convert LocalDate to SQL Date
            );

            // Save response to database
            reponseService.ajouter(reponse);
            System.out.println("✅ Response added successfully!");

            // Show success alert
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("✅ Response added successfully!");
            alert.showAndWait();

            // Clear form fields after success
            ReclamationM.getSelectionModel().clearSelection();
            dateReponse.setValue(null);
            messageReponse.clear();
            errorLabel.setText("");

            Stage stage = (Stage) save.getScene().getWindow();
            stage.close();

        } catch (SQLException e) {
            errorLabel.setText("❌ Error saving response: " + e.getMessage());
            System.err.println("Error: " + e.getMessage());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadReclamations();
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
