package Controllers;

import Entities.Reclamation;
import Entities.User;
import Services.ReclamationService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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

public class CardReclamationController implements Initializable {

    @FXML
    private Label dateReclamation;
    @FXML
    private Label user;
    @FXML
    private Button delete;
    @FXML
    private Label message;
    @FXML
    private Label type;
    @FXML
    private Button update;

    private Reclamation reclamation; // Store reclamation data
    private ReclamationService reclamationService = new ReclamationService(); // Service instance
    private ReclamationController parentController; // Reference to the main controller

    // Method to set data and link parent controller
    // Method to set data and link parent controller
    public void setData(Reclamation reclamation, ReclamationController parentController) {
        this.reclamation = reclamation;
        this.parentController = parentController;

        type.setText(reclamation.getType());
        message.setText(reclamation.getMessage());

        // Fetch user information (replace with actual method to get User)
        User userEntity = reclamationService.findUserById(reclamation.getIdUser());
        if (userEntity != null) {
            user.setText(userEntity.getNom()); // Display username instead of ID
        } else {
            user.setText("Unknown User"); // Fallback text
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(reclamation.getDateReclamation());
        dateReclamation.setText(formattedDate);
    }

    // Handle deletion
    @FXML
    void handledelete(ActionEvent event) {
        try {
            // Confirm before deleting
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Voulez-vous vraiment supprimer cette réclamation ?");
            alert.showAndWait();

            // Delete the reclamation from the database
            reclamationService.supprimer(reclamation.getId());

            // Refresh the list in the main controller
            parentController.loadReclamations();

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de supprimer la réclamation.");
        }
    }

    // Handle update action
    @FXML
    void handleupdate(ActionEvent event) {
        try {
            // Load the FXML file for update form
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/updateReclamation.fxml"));
            Parent root = loader.load();

            // Get the controller
            UpdateReclamationController updateController = loader.getController();

            // Pass the reclamation data to the update controller
            updateController.setReclamation(reclamation);

            // Show update window
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Update Reclamation");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Show alert method
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        VBox card = (VBox) type.getParent().getParent();
        card.getStylesheets().add(getClass().getResource("/css/cardStyle.css").toExternalForm());
    }




}
