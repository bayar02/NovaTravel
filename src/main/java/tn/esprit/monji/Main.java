package tn.esprit.monji;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            System.out.println("Starting application...");
            
            // Get the FXML URL directly from resources
            var fxmlUrl = Main.class.getResource("/fxml/main.fxml");
            if (fxmlUrl == null) {
                throw new RuntimeException("Cannot find /fxml/main.fxml");
            }
            System.out.println("Found FXML at: " + fxmlUrl);
            
            // Load the FXML
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();
            System.out.println("FXML loaded successfully");
            
            // Create scene
            Scene scene = new Scene(root);
            
            // Add stylesheet
            var cssUrl = Main.class.getResource("/icons/style.css");
            if (cssUrl == null) {
                throw new RuntimeException("Cannot find /style.css");
            }
            scene.getStylesheets().add(cssUrl.toExternalForm());
            System.out.println("Stylesheet added successfully");
            
            // Configure stage
            primaryStage.setTitle("Nova Travel Management");
            primaryStage.setScene(scene);
            primaryStage.setMaximized(true);
            primaryStage.show();
            System.out.println("Application started successfully");
            
        } catch (Exception e) {
            System.err.println("Error starting application:");
            System.err.println("Error message: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
} 