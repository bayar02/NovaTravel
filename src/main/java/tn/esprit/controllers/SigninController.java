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
import tn.esprit.utils.SessionManager;

import java.io.IOException;
import java.sql.SQLException;

public class SigninController {
    @FXML
    private TextField email;
    @FXML
    private PasswordField password;
    @FXML
    private Label errorLabel;
    @FXML
    private CheckBox rememberMe;

    private UserService userService;

    public SigninController() {
        userService = new UserService();
    }

    @FXML
    public void login(ActionEvent event) {
        String userEmail = email.getText().trim();
        String userPassword = password.getText().trim();
        
        if (userEmail.isEmpty() || userPassword.isEmpty()) {
            showError("Veuillez remplir tous les champs");
            return;
        }
        
        if (!ValidationService.isValidEmail(userEmail)) {
            showError("Format d'email invalide");
            return;
        }

        try {
            User user = userService.authenticate(userEmail, userPassword);
            if (user != null) {
                // Set the user in session
                SessionManager.getInstance().setCurrentUser(user);
                
                // Navigate based on user role
                String targetFxml = switch (user.getRole()) {
                    case ADMIN -> "fxml/acceuil_admin.fxml";
                    case REGULAR_USER -> "fxml/user_dashboard.fxml";
                    default -> "fxml/home.fxml";
                };
                
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/" + targetFxml));
                    Parent root = loader.load();
                    if (targetFxml.equals("fxml/user_dashboard.fxml")) {
                        UserDashboardController controller = loader.getController();
                        controller.setUserInformation(user.getMail()); // Pass user email to UserDashboardController
                    }
                    Stage stage = (Stage) email.getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (IOException e) {
                    showError("Erreur lors de la navigation");
                    e.printStackTrace();
                }
            } else {
                showError("Email ou mot de passe incorrect");
            }
        } catch (SQLException e) {
            showError("Erreur de connexion à la base de données");
            e.printStackTrace();
        }
    }

    @FXML
    public void goToSignup(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/signup.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) email.getScene().getWindow();
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