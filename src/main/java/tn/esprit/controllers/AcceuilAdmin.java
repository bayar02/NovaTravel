package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;

public class AcceuilAdmin {
    @FXML
    private Label label_welcome;
    @FXML
    private Label errorLabel;
    @FXML
    private Pane mainContent; // The area where new pages will load
    @FXML
    private TextField email;

    @FXML
    private Button btnDashboard, btnUsers, btnSettings, btnLogout;

    // Load different views into the main content area
    private void loadPage(String fxmlFile) {
        try {
            Parent page = FXMLLoader.load(getClass().getResource("/tn/esprit/views/" + fxmlFile));
            mainContent.getChildren().clear(); // Clear previous content
            mainContent.getChildren().add(page); // Load new content
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToReclamations() {
        loadPage("dashboard.fxml");
    }

    @FXML
    private void goToProfil() {
        loadPage("dashboard.fxml");
    }

    @FXML
    private void goToVols() {
        loadPage("dashboard.fxml");
    }

    @FXML
    public void goToUsers(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin_dashboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnLogout.getScene().getWindow();
            stage.close();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showError("Erreur lors de la navigation");
            e.printStackTrace();
        }
    }

    @FXML
    private void goToHebergements() {
        loadPage("settings.fxml");
    }

    @FXML
    private void logout() {
        // Close the current window (logout logic)
        Stage stage = (Stage) btnLogout.getScene().getWindow();
        stage.close();
    }
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
    public void setUserInformation(String email) {
        label_welcome.setText("Bienvenue " + email + "!");
    }

}
