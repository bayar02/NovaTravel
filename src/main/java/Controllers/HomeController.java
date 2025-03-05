package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {


    @FXML
    private AnchorPane contentPane; // This is where we will load reclamationList.fxml

    @FXML
    private void GoToReclamations() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/reclamationList.fxml"));
            Parent reclamationView = loader.load();
            contentPane.getChildren().clear();
            contentPane.getChildren().add(reclamationView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void GoToReponses(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/reponseList.fxml"));
            Parent reponseView = loader.load();
            contentPane.getChildren().clear();
            contentPane.getChildren().add(reponseView);
        } catch (IOException e) {
            e.printStackTrace();
        }    }

    private void loadScene(ActionEvent event, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Gestion Reclamations");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // For debugging
        }
    }
    @FXML
    private void openChatBot(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/chatbot.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("ChatBot");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GoToReclamations(); // Load Reclamation list by default

    }
}
