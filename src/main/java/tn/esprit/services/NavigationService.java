package tn.esprit.services;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class NavigationService {
    public static void navigateTo(ActionEvent event, String fxml, String title) {
        try {
            Parent root = FXMLLoader.load(NavigationService.class.getResource("/" + fxml));
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setTitle(title);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Add proper error handling
        }
    }
} 