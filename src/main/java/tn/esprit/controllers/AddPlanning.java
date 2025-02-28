package tn.esprit.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.entities.Event;
import tn.esprit.entities.Planning;
import tn.esprit.service.ServiceEvent;
import tn.esprit.service.ServicePlanning;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddPlanning {
    @FXML
    private DatePicker date_creation_tf;

    @FXML
    private TextField nom_tf;

    @FXML
    private TextField events_tf;

    @FXML
    private ListView<String> eventListView;

    @FXML
    private Label messageLabel;

    private final ServicePlanning servicePlanning = new ServicePlanning();
    private final ServiceEvent serviceEvent = new ServiceEvent();
    private ObservableList<Event> availableEvents = FXCollections.observableArrayList();


    @FXML
    public void initialize() {
        // Set the ListView to allow multiple selections
        eventListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // Load available events into the ListView
        try {
            List<Event> events = serviceEvent.afficher();
            for (Event event : events) {
                eventListView.getItems().add(event.getNom());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            messageLabel.setText("Erreur de chargement des événements.");
        }
    }

    @FXML
    private void ajouterPlanning() {
        String nom = nom_tf.getText();
        String dateCreation = date_creation_tf.getValue().toString();

        ObservableList<String> selectedEvents = eventListView.getSelectionModel().getSelectedItems();

        ServiceEvent serviceEvent = new ServiceEvent();
        List<Event> eventsToAdd = new ArrayList<>();

        try {
            List<Event> allEvents = serviceEvent.afficher();
            for (String selectedEvent : selectedEvents) {
                for (Event event : allEvents) {
                    if (event.getNom().equals(selectedEvent)) {
                        eventsToAdd.add(event);
                        break;
                    }
                }
            }

            Planning planning = new Planning();
            planning.setNom(nom);
            planning.setDateCreation(dateCreation);
            planning.setEvents(eventsToAdd);

            ServicePlanning servicePlanning = new ServicePlanning();
            servicePlanning.ajouter(planning);

            messageLabel.setText("Planning ajouté avec succès !");
        } catch (SQLException e) {
            e.printStackTrace();
            messageLabel.setText("Erreur lors de l'ajout du planning.");
        }
    }


    @FXML
    private void clearPlanningFields() {
        nom_tf.clear();
        events_tf.clear();
        date_creation_tf.setValue(null);
        eventListView.getSelectionModel().clearSelection();
        messageLabel.setText("");
    }


    @FXML
    private void showMessage(String message, boolean isError) {
        if (messageLabel != null) {
            messageLabel.setText(message);
            messageLabel.setStyle(isError ? "-fx-text-fill: red;" : "-fx-text-fill: green;");
        }
    }


    public void afficherPlanning(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ShowPlanning.fxml"));
            Parent root = loader.load();
            Scene currentScene = ((Node) actionEvent.getSource()).getScene();
            currentScene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

   /* public void showWeather(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/weatherAPI.fxml"));
            Parent root = loader.load();
            Scene currentScene = ((Node) actionEvent.getSource()).getScene();
            currentScene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    public void goToWeatherPage(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/weatherAPI.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Weather Application");
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}
