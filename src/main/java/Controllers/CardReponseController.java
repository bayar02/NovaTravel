package Controllers;

import Entities.Reponse;
import Services.ReclamationService;
import Services.ReponseService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

public class CardReponseController implements Initializable {


    private ReponseService reponseService = new ReponseService(); // Service instance
    private ReclamationService reclamationService = new ReclamationService(); // Added ReclamationService


    @FXML
    private Label dateReponse;

    @FXML
    private Button delete;

    @FXML
    private Label message;

    @FXML
    private Label reclamation;

    private Reponse reponse;
    private ReponseController parentController;

    @FXML
    private Button update;

    public void setData(Reponse reponse, ReponseController parentController) {
        this.reponse = reponse;
        this.parentController = parentController;

        message.setText(reponse.getMessage());

        // Format date
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(reponse.getDateReponse());
        dateReponse.setText(formattedDate);

        try {
            // Fetch the reclamation message using the reclamation ID
            String reclamationMessage = reclamationService.getReclamationMessageById(reponse.getIdReclamation());
            reclamation.setText(reclamationMessage); // Set the reclamation message in the label
        } catch (SQLException e) {
            reclamation.setText("Erreur chargement réclamation"); // Error handling
            e.printStackTrace();
        }
    }

    @FXML
    void handledelete(ActionEvent event) {
        try {
            // Confirm before deleting
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Voulez-vous vraiment supprimer cette reponse ?");
            alert.showAndWait();

            // Delete the reclamation from the database
            reponseService.supprimer(reponse.getId());

            // Refresh the list in the main controller
            parentController.loadResponses();

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de supprimer la réclamation.");
        }
    }


    @FXML
    void handleupdate(ActionEvent event) {
        try {
            // Load the FXML file for update form
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/updateReponse.fxml"));
            Parent root = loader.load();

            // Get the controller
            UpdateReponseController updateController = loader.getController();

            // Pass the reclamation data to the update controller
            updateController.setReponses(reponse);

            // Show update window
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Update Reclamation");
            stage.show();
        } catch (IOException e) {
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        VBox card = (VBox) reclamation.getParent().getParent();
        card.getStylesheets().add(getClass().getResource("/css/cardStyle.css").toExternalForm());
    }
}
