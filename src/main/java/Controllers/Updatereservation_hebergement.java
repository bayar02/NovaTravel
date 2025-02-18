package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import models.reservation_hebergement;
import models.reservation_hebergement;
import services.Servicereservation_hebergement;
import services.Servicehebergement;
import services.Servicereservation_hebergement;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;



public class Updatereservation_hebergement{

    @FXML
    private DatePicker date_debut;

    @FXML
    private DatePicker date_fin;

    @FXML
    private ComboBox<String> id_hebergement;

    @FXML
    private TextField nb_perso;

    private reservation_hebergement selectedRH;

    private final Servicereservation_hebergement servicereservation_hebergement;


    @FXML
    void initialize() {
        loadhebergementID();
    }

    @FXML
    void showreservationhebergement(ActionEvent event) {

    }




    public Updatereservation_hebergement() {
        this.servicereservation_hebergement = new Servicereservation_hebergement();
    }



    public void setreservtion_hebergementDetails(reservation_hebergement RH) {
        this.selectedRH = RH;

        // Debug: Print the values
        System.out.println("Selected reservation_hebergement: " + RH);

        int idRH= RH.getId();
        // Set the ComboBox with the pack ID
        String id_hebergementt = String.valueOf(RH.getId_hebergement());
        id_hebergement.setValue(id_hebergementt);

        // Set the number of people
        nb_perso.setText(String.valueOf(RH.getNb_perso())); // Added nb_perso field


        // Convert java.sql.Date to LocalDate correctly
        if (RH.getDate_debut() != null) {
            date_debut.setValue(RH.getDate_debut().toLocalDate());
        } else {
            System.out.println("⚠ date_debut is NULL!");
        }

        if (RH.getDate_fin() != null) {
            date_fin.setValue(RH.getDate_fin().toLocalDate());
        } else {
            System.out.println("⚠ date_fin is NULL!");
        }
    }


    public void loadhebergementID() {
        Servicehebergement servicehebergement = new Servicehebergement();
        List<String> hebergementIDs = servicehebergement.getAllhebergementID();
        id_hebergement.getItems().clear();
        id_hebergement.getItems().addAll(hebergementIDs);
        id_hebergement.setPromptText("Sélectionner un hébergement");
    }

    @FXML
    void save(ActionEvent event) {
        String selectedHebergement = id_hebergement.getValue();
        LocalDate newDateDebut = date_debut.getValue();
        LocalDate newDateFin = date_fin.getValue();
        String nbPersonnesStr = nb_perso.getText().trim();

        // Vérification si les champs sont vides
        if (selectedHebergement == null || newDateDebut == null || newDateFin == null || nbPersonnesStr.isEmpty()) {
            showAlert("Erreur", "⚠ Tous les champs doivent être remplis !");
            return;
        }

        // Vérification que la date de souscription est avant la date d'expiration
        if (newDateDebut.isAfter(newDateFin)) {
            showAlert("Erreur", "⚠ La date de début doit être avant la date de fin !");
            return;
        }

        // Vérification que la date de souscription n'est pas avant aujourd'hui
        if (newDateDebut.isBefore(LocalDate.now())) {
            showAlert("Erreur", "⚠ La date de début ne peut pas être avant aujourd'hui !");
            return;
        }

        int nbPersonnes;
        try {
            nbPersonnes = Integer.parseInt(nbPersonnesStr);
            if (nbPersonnes <= 0) {
                showAlert("Erreur", "⚠ Le nombre de personnes doit être supérieur à 0 !");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Erreur", "⚠ Nombre de personnes invalide !");
            return;
        }

        Date sqlDateDebut = Date.valueOf(newDateDebut);
        Date sqlDateFin = Date.valueOf(newDateFin);
        int id_hebergement = Integer.parseInt(selectedHebergement);


        try {
            selectedRH.setId_hebergement(id_hebergement);
            selectedRH.setDate_debut(sqlDateDebut);
            selectedRH.setDate_fin(sqlDateFin);
            selectedRH.setNb_perso(nbPersonnes);

            // Mise à jour de l'abonnement
            servicereservation_hebergement.update(selectedRH);
            showInfo("Succès", "✅ hebergement mis à jour avec succès !");
        } catch (NumberFormatException e) {
            showAlert("Erreur", "ID de l'hebergement invalide !");
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors de la mise à jour : " + e.getMessage());
        }
    }

    // Fonction pour afficher une alerte
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}