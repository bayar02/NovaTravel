package tn.esprit.controllers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import tn.esprit.entities.Event;
import tn.esprit.service.ServiceEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class GestionEvents {


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




    private ObservableList<Event> eventList = FXCollections.observableArrayList();
    private Event selectedEvent;
    private final ServiceEvent se = new ServiceEvent();


    @FXML
    private void showMessage(String message, boolean isError) {

        if (messageLabel != null) {
            messageLabel.setText(message);
            messageLabel.setStyle(isError ? "-fx-text-fill: red;" : "-fx-text-fill: green;");
        }
    }

    @FXML
    public void ajouterEvent(ActionEvent actionEvent) throws SQLException {

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
        else {
            try {
                ServiceEvent se = new ServiceEvent();
                //cast el prix to float ( conversion issue )
                se.ajouter(new Event(nom_tf.getText(), desc_tf.getText(), lieu_tf.getText(), date_tf.getValue().toString(), Integer.parseInt(duree_tf.getText()), (float) Double.parseDouble(prix_tf.getText())));
                showMessage("Événement ajouté avec succès", false);
                clearEventFields(); // Efface les champs après l'ajout réussi
            } catch (Exception e) {
                showMessage("Erreur lors de l'ajout de l'événement : " + e.getMessage(), true);


            }
        }
    }

    @FXML

    private void clearEventFields () {
        nom_tf.clear();
        desc_tf.clear();
        lieu_tf.clear();
        date_tf.setValue(null);
        duree_tf.clear();
        prix_tf.clear();
        messageLabel.setText("");
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

