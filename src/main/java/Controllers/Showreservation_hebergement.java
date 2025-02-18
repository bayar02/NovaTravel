package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.reservation_hebergement;
import models.hebergement;
import services.Servicereservation_hebergement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import services.Servicehebergement;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


import javafx.scene.control.TextField;

public class Showreservation_hebergement {

    @FXML
    private VBox DetailsP;

    @FXML
    private Label date_debut;

    @FXML
    private Label date_fin;

    @FXML
    private TextField idrecher;

    @FXML
    private ListView <reservation_hebergement> listview;

    @FXML
    private Label nb_perso;

    @FXML
    private Label id_hebergement;



  /*  @FXML
    void updatereservationhebergement(ActionEvent event) {

    }*/




    private final Servicereservation_hebergement servicereservation_hebergement = new Servicereservation_hebergement();

    @FXML
    void initialize() throws SQLException {
        loadreservation_hebergement();
        listview.setOnMouseClicked(event -> showreservation_hebergementDetails());
    }

    private void loadreservation_hebergement() {
        ObservableList<reservation_hebergement> reservation_hebergementsList = FXCollections.observableArrayList();

        try {
            List<reservation_hebergement> reservation_hebergements = servicereservation_hebergement.getAll();  // Fetch packs from database
            reservation_hebergementsList.addAll(reservation_hebergements);  // Add all packs to the observable list
        } catch (SQLException e) {
            e.printStackTrace();
        }

        listview.setItems(reservation_hebergementsList);

        // Set a custom cell factory to format how packs are displayed
        listview.setCellFactory(param -> new ListCell<reservation_hebergement>() {
            @Override
            protected void updateItem(reservation_hebergement item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText("ID: " + item.getId() +
                            ", Hébergement: " + item.getId_hebergement() +
                            ", Début: " + item.getDate_debut()+
                            ", Fin: " + item.getDate_fin() +
                            ", Personnes: " + item.getNb_perso());
                }
            }
        });
    }

    private void showreservation_hebergementDetails() {
        reservation_hebergement selectedreservation_hebergement = listview.getSelectionModel().getSelectedItem();
        if (selectedreservation_hebergement != null) {
            id_hebergement.setText(String.valueOf(selectedreservation_hebergement.getId_hebergement()));
            date_debut.setText(selectedreservation_hebergement.getDate_debut().toString());
            date_fin.setText(selectedreservation_hebergement.getDate_fin().toString());
            nb_perso.setText(String.valueOf(selectedreservation_hebergement.getNb_perso()));
        }
    }

    @FXML
    void supprimerreservationhebergement(ActionEvent event) {
        reservation_hebergement selectedRH = listview.getSelectionModel().getSelectedItem();
        if (selectedRH != null) {
            Servicereservation_hebergement serviceRH = new Servicereservation_hebergement(); // Service de suppression de réservation
            try {
                // Suppression du pack de la base de données
                serviceRH.delete(selectedRH.getId());

                // Retirer le pack de la ListView pour mettre à jour l'affichage
                listview.getItems().remove(selectedRH);
                listview.refresh();

                System.out.println("✅ Réservation supprimée avec succès !");
                loadreservation_hebergement();// Confirmation dans la console
            } catch (SQLException e) {
                System.out.println("❌ Erreur : La réservation n'a pas pu être supprimée de la base de données.");
                e.printStackTrace();  // Pour plus de détails sur l'erreur
            }
        } else {
            System.out.println("⚠ Aucune réservation sélectionnée. Veuillez sélectionner une réservation à supprimer.");
        }

    }

    @FXML
    void updateReservationHebergement(ActionEvent event) {
        reservation_hebergement selectedRH = listview.getSelectionModel().getSelectedItem();

        if (selectedRH != null) {
            try {
                // Open the update window with the selected Pack
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Updatereservation_hebergement.fxml"));
                Parent root = loader.load();

                Updatereservation_hebergement updateRHController = loader.getController();
                updateRHController.setreservtion_hebergementDetails(selectedRH);

                Scene currentScene = ((Node) event.getSource()).getScene();
                currentScene.setRoot(root);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("⚠ Please select a reservation to modify.");
        }

    }
    @FXML
    void reload(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Showreservation_hebergement.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void retour(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Addreservation_hebergement.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

    }
}