package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.entities.User;
import tn.esprit.services.UserService;

public class UserFormController {
    @FXML private TextField cin;
    @FXML private TextField nom;
    @FXML private TextField prenom;
    @FXML private TextField tel;
    @FXML private TextField mail;
    @FXML private ChoiceBox<String> role;
    @FXML private PasswordField password; // Added PasswordField
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
            // Set the password if the user is not null
            // (Note: You might want to hide the password field for existing users, depending on your use case)
        }
    }

    @FXML
    private void handleSave() {
        if (cin.getText().isEmpty() || nom.getText().isEmpty() ||
                prenom.getText().isEmpty() || tel.getText().isEmpty() ||
                mail.getText().isEmpty() || role.getValue() == null || password.getText().isEmpty()) { // Check if password is empty
            errorLabel.setText("Tous les champs doivent être remplis !");
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
        user.setPassword(password.getText()); // Set the password field

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
}
