package Controllers;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import models.reservation_hebergement;
import models.reservation_hebergement;
import services.Servicereservation_hebergement;
import models.hebergement;
import services.Servicehebergement;

import static java.sql.Date.*;


public class Addreservation_hebergement {

    @FXML
    private DatePicker date_debut;

    @FXML
    private DatePicker date_fin;

    @FXML
    private ComboBox<String> id_hebergement;

    @FXML
    private TextField nb_perso;

    private Servicereservation_hebergement servicereservation_hebergement = new Servicereservation_hebergement();





    @FXML
    void initialize() {

        loadhebergementID();

    }


    public void loadhebergementID() {

        Servicehebergement servicehebergement = new Servicehebergement();
        List<String> hebergementNames = servicehebergement.getAllhebergementID();

        System.out.println("hebergement names fetched: " + hebergementNames);

        id_hebergement.getItems().clear();
        id_hebergement.getItems().addAll(hebergementNames);

        id_hebergement.setPromptText("Sélectionner un hebergement");

        if (!hebergementNames.isEmpty()) {
            id_hebergement.setValue(hebergementNames.get(0));
        }
    }

    @FXML
    void save(ActionEvent event) {
        try {
            // Récupération des valeurs saisies
            String selectedHebergement = id_hebergement.getValue();
            LocalDate newDateDebut = date_debut.getValue();
            LocalDate newDateFin = date_fin.getValue();
            String nbPersonnesStr = nb_perso.getText().trim();

            // Vérification si les champs sont vides
            if (selectedHebergement == null || newDateDebut == null || newDateFin == null || nbPersonnesStr.isEmpty()) {
                showAlert("Erreur", "⚠ Tous les champs doivent être remplis !");
                return;
            }

            // Vérification que la date de souscription n'est pas avant aujourd'hui
            if (newDateDebut.isBefore(LocalDate.now())) {
                showAlert("Erreur", "⚠ La date de debut ne peut pas être antérieure à aujourd'hui !");
                return;
            }

            // Vérification que la date de souscription est avant la date d'expiration
            if (newDateDebut.isAfter(newDateFin)) {
                showAlert("Erreur", "⚠ La date de debut doit être avant la date de fin !");
                return;
            }


            // Debugging: Affichage des valeurs avant enregistrement
            System.out.println("ID Hébergement: " + id_hebergement);
            System.out.println("Date Début: " + date_debut);
            System.out.println("Date Fin: " + date_fin);
            System.out.println("Nombre de Personnes: " + nb_perso);

            // Conversion du pack ID en entier
            int idHebergement;
            try {
                idHebergement = Integer.parseInt(selectedHebergement);
            } catch (NumberFormatException e) {
                showAlert("Erreur", "⚠ ID de l'hébergement invalide !");
                return;
            }
            int nbrpersonnes = Integer.parseInt(nbPersonnesStr);


            // Conversion des LocalDate en SQL Date
            Date sqlDateDebut = Date.valueOf(newDateDebut);
            Date sqlDateFin = Date.valueOf(newDateFin);

            // Création de l'objet Abonnement
            reservation_hebergement reservation = new reservation_hebergement(idHebergement, sqlDateDebut, sqlDateFin,nbrpersonnes );

            // Enregistrement dans la base de données
            servicereservation_hebergement.create(reservation);

            // Affichage du message de succès
            showAlert("Succès", "✅ Réservation enregistrée avec succès !");

        } catch (SQLException e) {
            showAlert("Erreur", "⚠ Erreur SQL : " + e.getMessage());
        } catch (Exception e) {
            showAlert("Erreur", "⚠ Une erreur est survenue : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }


    @FXML
    void showreservationhebergement(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Showreservationhebergement.fxml"));
            Parent root = loader.load();

            // Remplacer la scène actuelle
            Scene currentScene = ((Node) event.getSource()).getScene();
            currentScene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }




}