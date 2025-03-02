package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import tn.esprit.utils.SessionManager;
import javafx.event.ActionEvent;

import java.io.IOException;

public class UserDashboardController {
    @FXML
    private Label label_welcome;

    @FXML
    private void handleLogout(ActionEvent event) {
        SessionManager.getInstance().clearSession();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/signin.fxml"));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setUserInformation(String email) {
        label_welcome.setText("Bienvenue " + email + "!");
    }
    @FXML
    private void handleProfile(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/UserProfile.fxml"));
            Parent root = loader.load();

            // Pass user data to profile controller
            UserProfileController profileController = loader.getController();
            profileController.setUser(SessionManager.getInstance().getCurrentUser());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}