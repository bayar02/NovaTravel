package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import tn.esprit.utils.EmailSender;
import tn.esprit.utils.PasswordGenerator;
import tn.esprit.utils.SecurityUtil;
import tn.esprit.utils.MyDataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ForgotPasswordController {

    @FXML
    private TextField emailField;
    @FXML
    private Label messageLabel;
    @FXML
    private Button sendButton;

    @FXML
    private void sendResetEmail() {
        String userEmail = emailField.getText().trim();

        if (userEmail.isEmpty()) {
            messageLabel.setText("Veuillez entrer votre email.");
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setVisible(true);
            return;
        }

        // Generate a secure new password
        String newPassword = PasswordGenerator.generateSecurePassword();

        // Hash the new password before storing it (for security)
        String hashedPassword = SecurityUtil.hashPassword(newPassword);

        // Update the password in the database
        boolean isUpdated = updatePasswordInDatabase(userEmail, hashedPassword);

        if (isUpdated) {
            // Send an email with the new password
            boolean emailSent = EmailSender.sendEmail(userEmail, "Réinitialisation du mot de passe",
                    "Votre nouveau mot de passe est : " + newPassword +
                            "\nVeuillez le modifier après connexion.");

            if (emailSent) {
                messageLabel.setText("Un email avec votre nouveau mot de passe a été envoyé.");
                messageLabel.setStyle("-fx-text-fill: green;");
            } else {
                messageLabel.setText("Erreur: L'email n'a pas pu être envoyé.");
                messageLabel.setStyle("-fx-text-fill: red;");
            }
        } else {
            messageLabel.setText("Erreur: Impossible de mettre à jour le mot de passe.");
            messageLabel.setStyle("-fx-text-fill: red;");
        }

        messageLabel.setVisible(true);
    }

    /**
     * Updates the user's password in the database.
     *
     * @param email        The user's email address.
     * @param newPassword  The new hashed password.
     * @return true if update is successful, false otherwise.
     */
    private boolean updatePasswordInDatabase(String email, String newPassword) {
        String sql = "UPDATE user SET password = ? WHERE mail = ?";

        try (Connection conn = MyDataBase.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newPassword);
            pstmt.setString(2, email);

            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;  // Returns true if at least one row was updated

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
