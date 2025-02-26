package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.entities.User;
import tn.esprit.services.UserService;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserFormController {
    @FXML private TextField cin;
    @FXML private TextField nom;
    @FXML private TextField prenom;
    @FXML private TextField tel;
    @FXML private TextField mail;
    @FXML private ChoiceBox<String> role;
    @FXML private PasswordField password;
    @FXML private Button save;
    @FXML private Button cancel;
    @FXML private Label errorLabel;

    private UserService userService;
    private User user;

    public UserFormController() {
        this.userService = new UserService();
    }

    @FXML
    public void initialize() {
        role.getItems().addAll("ADMIN", "REGULAR_USER", "AGENT");
        errorLabel.setVisible(false);
    }

    public void setUser(User user) {
        this.user = user;
        if (user != null) {
            cin.setText(user.getCin());
            nom.setText(user.getNom());
            prenom.setText(user.getPrenom());
            tel.setText(user.getTel());
            mail.setText(user.getMail());
            role.setValue(user.getRole().toString());

        }
    }

    @FXML
    private void handleSave() {
        String errorMessage = validateForm();
        if (errorMessage != null) {
            errorLabel.setText(errorMessage);
            errorLabel.setVisible(true);
            return;
        }

        if (user == null) {
            user = new User();
        }

        user.setCin(cin.getText());
        user.setNom(nom.getText());
        user.setPrenom(prenom.getText());
        user.setTel(tel.getText());
        user.setMail(mail.getText());
        user.setRole(User.Role.valueOf(role.getValue()));
        user.setPassword(password.getText());

        if (user.getId() == 0) {
            userService.ajouter(user);
        } else {
            userService.modifier(user);
        }

        ((Stage) save.getScene().getWindow()).close();
    }

    @FXML
    private void handleCancel() {
        ((Stage) cancel.getScene().getWindow()).close();
    }

    private String validateForm() {
        if (cin.getText().isEmpty() || nom.getText().isEmpty() ||
                prenom.getText().isEmpty() || tel.getText().isEmpty() ||
                mail.getText().isEmpty() || role.getValue() == null || password.getText().isEmpty()) {
            return "Tous les champs doivent être remplis !";
        }

        // Validate CIN (must be 8 digits)
        if (!cin.getText().matches("\\d{8}")) {
            return "Le CIN doit contenir exactement 8 chiffres.";
        }

        // Validate email
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(mail.getText());
        if (!matcher.matches()) {
            return "L'email doit être valide.";
        }

        // Validate password (must be at least 8 characters)
        if (password.getText().length() < 8) {
            return "Le mot de passe doit contenir au moins 8 caractères.";
        }

        return null;
    }
}
