package tn.esprit.monji.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;
import javafx.scene.Parent;
import javafx.scene.control.ProgressIndicator;
import javafx.application.Platform;
import java.io.IOException;

public class MainController {
    @FXML
    private StackPane contentArea;
    
    private ProgressIndicator loadingIndicator;
    
    @FXML
    public void initialize() {
        // Initialize loading indicator
        loadingIndicator = new ProgressIndicator();
        loadingIndicator.setMaxSize(50, 50);
        loadingIndicator.setVisible(false);
        contentArea.getChildren().add(loadingIndicator);
        
        // Show flights view by default
        showFlights();
    }
    
    @FXML
    private void showFlights() {
        loadView("/fxml/vol.fxml");
    }
    
    @FXML
    private void showReservations() {
        loadView("/fxml/reservation_vol.fxml");
    }
    
    private void loadView(String fxmlPath) {
        // Show loading indicator
        loadingIndicator.setVisible(true);
        
        // Load view in background thread
        new Thread(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
                Parent view = loader.load();
                
                // Update UI in JavaFX thread
                Platform.runLater(() -> {
                    contentArea.getChildren().clear();
                    contentArea.getChildren().add(view);
                    contentArea.getChildren().add(loadingIndicator);
                    loadingIndicator.setVisible(false);
                });
            } catch (IOException e) {
                Platform.runLater(() -> {
                    loadingIndicator.setVisible(false);
                    System.err.println("Error loading view: " + e.getMessage());
                    e.printStackTrace();
                });
            }
        }).start();
    }
} 