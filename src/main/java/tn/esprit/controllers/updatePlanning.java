package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import tn.esprit.entities.Event;
import tn.esprit.entities.Planning;
import tn.esprit.service.ServicePlanning;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class updatePlanning {

    @FXML
    private TextField nom_tf;

    @FXML
    private DatePicker date_creation_tf;

    @FXML
    private ListView<Event> eventListView;

    @FXML
    private Label messageLabel;

    private int planningId;

    public void preloadPlanningData(Planning planning) {
        System.out.println("preloading el planning id " + planning.getId());
        this.planningId = planning.getId();

        nom_tf.setText(planning.getDateCreation());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(planning.getNom(), formatter);
       date_creation_tf.setValue(date);

        eventListView.getItems().addAll(planning.getEvents());
    }


    public void UpdatePlanning(ActionEvent actionEvent) {

        LocalDate dateCreation = date_creation_tf.getValue();
        String nom = nom_tf.getText();
        List<Event> selectedEvents = eventListView.getSelectionModel().getSelectedItems();


        if (nom.isEmpty() || dateCreation == null || selectedEvents.isEmpty()) {
            messageLabel.setText("Veuillez remplir tous les champs.");
            return;
        }

        try {

            Planning updatedPlanning = new Planning();

            updatedPlanning.setId(planningId);
            System.out.println("el planning id changé  " + planningId);

            updatedPlanning.setDateCreation(String.valueOf(java.sql.Date.valueOf(dateCreation)));
            updatedPlanning.setNom(nom);
            updatedPlanning.setEvents(selectedEvents);


            ServicePlanning planningService = new ServicePlanning();
            planningService.modifier(updatedPlanning);

            messageLabel.setText("Planning mis à jour avec succès !");
            clearFields();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private void clearFields() {
        nom_tf.clear();
        date_creation_tf.setValue(null);
        eventListView.getSelectionModel().clearSelection();
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
}
