package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import tn.esprit.entities.Event;
import tn.esprit.service.ServiceEvent;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class updateEvent {


    @FXML
    private DatePicker date_tf;

    @FXML
    private TextField desc_tf;

    @FXML
    private TextField duree_tf;

    @FXML
    private TextField lieu_tf;

    @FXML
    private TextField nom_tf;

    @FXML
    private TextField prix_tf;

    @FXML
    private Label messageLabel;

    private int eventId;


    public void UpdateEvent(ActionEvent actionEvent) {
        // Get the values from the text fields
        String nom = nom_tf.getText();
        String desc = desc_tf.getText();
        String lieu = lieu_tf.getText();
        LocalDate date = date_tf.getValue();
        String dureeStr = duree_tf.getText();
        String prixStr = prix_tf.getText();

        // Validate inputs
        if (nom.isEmpty() || desc.isEmpty() || lieu.isEmpty() || date == null || dureeStr.isEmpty() || prixStr.isEmpty()) {
            messageLabel.setText("Veuillez remplir tous les champs.");
            return;
        }

        try {
            int duree = Integer.parseInt(dureeStr);
            double prix = Double.parseDouble(prixStr);

            // Create an Event object with the updated values
            Event updatedEvent = new Event();

            updatedEvent.setId(eventId);
            System.out.println("haw l event id changé  " + eventId);
            updatedEvent.setNom(nom);
            updatedEvent.setDescription(desc);
            updatedEvent.setLieu(lieu);
            updatedEvent.setDateEvent(String.valueOf(java.sql.Date.valueOf(date)));
            updatedEvent.setDuree(duree);
            updatedEvent.setPrix((float) prix);



            // Call the service layer to update the event
            ServiceEvent eventService = new ServiceEvent();
            eventService.modifier(updatedEvent);


            messageLabel.setText("Événement mis à jour avec succès !");
            clearFields();
        } catch (NumberFormatException e) {
            messageLabel.setText("Veuillez entrer des valeurs numériques valides pour la durée et le prix.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    public void preloadEventData(Event event) {
        // Ensure you are passing an Event object and not another type
        System.out.println("preloading el event " + event.getId());
        this.eventId = event.getId();  // Store the ID for updating

        nom_tf.setText(event.getNom());
        desc_tf.setText(event.getDescription());
        lieu_tf.setText(event.getLieu());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(event.getDateEvent(), formatter);
        date_tf.setValue(date);

        duree_tf.setText(String.valueOf(event.getDuree()));
        prix_tf.setText(String.valueOf(event.getPrix()));
    }

    private void clearFields() {
        nom_tf.clear();
        desc_tf.clear();
        lieu_tf.clear();
        date_tf.setValue(null);
        duree_tf.clear();
        prix_tf.clear();
    }

    public void afficherEvent(ActionEvent actionEvent) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ShowEvent.fxml"));
            Parent root = loader.load();
            Scene currentScene = ((Node) actionEvent.getSource()).getScene();
            currentScene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
