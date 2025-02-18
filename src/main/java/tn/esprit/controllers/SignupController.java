package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.entities.User;
import tn.esprit.services.UserService;
import tn.esprit.services.ValidationService;

import java.io.IOException;
import java.sql.SQLException;

public class SignupController {
    @FXML
    private TextField cin;
    @FXML
    private TextField nom;
    @FXML
    private TextField prenom;
    @FXML
    private TextField tel;
    @FXML
    private TextField mail;
    @FXML
    private PasswordField password;
    @FXML
    private PasswordField confirmPassword;
    @FXML
    private Label errorLabel;

    private UserService userService;

    public SignupController() {
        userService = new UserService();
    }

    @FXML
    public void handleSignup(ActionEvent event) {
        String userCin = cin.getText().trim();
        String userNom = nom.getText().trim();
        String userPrenom = prenom.getText().trim();
        String userTel = tel.getText().trim();
        String userMail = mail.getText().trim();
        String userPassword = password.getText();
        String confirmUserPassword = confirmPassword.getText();
        
        // Validate all fields
        if (userCin.isEmpty() || userNom.isEmpty() || userPrenom.isEmpty() || 
            userTel.isEmpty() || userMail.isEmpty() || userPassword.isEmpty() || 
            confirmUserPassword.isEmpty()) {
            showError("Veuillez remplir tous les champs");
            return;
        }
        
        if (!ValidationService.isValidEmail(userMail)) {
            showError("Format d'email invalide");
            return;
        }
        
        if (!ValidationService.isValidPassword(userPassword)) {
            showError("Le mot de passe doit contenir au moins 8 caractères");
            return;
        }
        
        if (!userPassword.equals(confirmUserPassword)) {
            showError("Les mots de passe ne correspondent pas");
            return;
        }
        
        try {
            // Create new user with REGULAR_USER role
            User newUser = new User(userCin, userNom, userPrenom, userTel, userMail, userPassword, User.Role.REGULAR_USER);
            
            if (userService.register(newUser)) {
                // Registration successful, redirect to login
                goToLogin(event);
            } else {
                showError("Erreur lors de l'inscription");
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate entry")) {
                showError("Cet email est déjà utilisé");
            } else {
                showError("Erreur de connexion à la base de données");
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void goToLogin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/signin.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) mail.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showError("Erreur lors de la navigation");
            e.printStackTrace();
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
} 