package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import tn.esprit.utils.MyDataBase;

import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    @FXML
    private Button button_logout;

    @FXML
    private Label label_welcome;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialization code if needed
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        MyDataBase.changeScene(event, "signin.fxml", "Login", null);
    }

    public void setUserInformation(String email) {
        label_welcome.setText("Bienvenue " + email + "!");
    }
}
