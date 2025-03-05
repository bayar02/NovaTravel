package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.esprit.entities.User;
import tn.esprit.services.ServiceUser;
import tn.esprit.services.UserService;
import tn.esprit.utils.SessionManager;

import java.io.IOException;
import java.sql.SQLException;

public class UserProfileController {
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField cinField;
    @FXML private TextField telField;
    private User currentUser;
    private ServiceUser serviceUser = new ServiceUser();
    @FXML
    public void initialize() {
        currentUser = SessionManager.getInstance().getCurrentUser();

        if (currentUser != null) {
            emailField.setText(currentUser.getMail());
            passwordField.setText(currentUser.getPassword());
            nomField.setText(currentUser.getNom());
            prenomField.setText(currentUser.getPrenom());
            cinField.setText(currentUser.getCin());
            telField.setText(currentUser.getTel());
        }
    }
    public UserProfileController() {
        serviceUser = new UserService();
    }

    public void setUser(User user) {
        this.currentUser = user;
        emailField.setText(user.getMail());
    }

    @FXML
    private void handleSaveProfile() throws SQLException {
        if (currentUser != null) {
            currentUser.setMail(emailField.getText());
            currentUser.setPassword(passwordField.getText());
            currentUser.setNom(nomField.getText());
            currentUser.setPrenom(prenomField.getText());
            currentUser.setCin(cinField.getText());
            currentUser.setTel(telField.getText());

            serviceUser.updateUser(currentUser);
            System.out.println("✅ Profil mis à jour avec succès!");

            // Go back to dashboard
            handleBack();
        }
    }

    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/user_dashboard.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Tableau de Bord");
            stage.show();
        } catch (IOException e) {
            System.err.println("❌ Erreur de navigation: " + e.getMessage());
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) emailField.getScene().getWindow();
        stage.close();
    }
}
