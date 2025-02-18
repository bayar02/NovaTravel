package Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import models.hebergement;
import services.Servicehebergement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class Showhebergement {

    @FXML
    private VBox DetailsP;

    @FXML
    private Label adresse;

    @FXML
    private Label desc;

    @FXML
    private TextField idrecher;

    @FXML
    private ListView<hebergement> listview; // Correction ici (éviter '?')

    @FXML
    private Label nomhebergement;

    @FXML
    private Label priix;

    @FXML
    private Label type;

    private final Servicehebergement servicehebergement = new Servicehebergement();

    @FXML
    void initialize() {
        System.out.println("Initialisation de Showhebergement...");
        loadhebergement();

        // Gérer la sélection d'un élément dans la ListView
        listview.setOnMouseClicked(event -> showhebergementDetails());
    }

    private void loadhebergement() {
        ObservableList<hebergement> hebergementList = FXCollections.observableArrayList();

        try {
            List<hebergement> packs = servicehebergement.getAll();
            hebergementList.addAll(packs); // Correction ici (packs au lieu de hebergement)
        } catch (SQLException e) {
            e.printStackTrace();
        }

        listview.setItems(hebergementList);

        // Personnalisation de l'affichage dans la ListView
        listview.setCellFactory(param -> new ListCell<hebergement>() {
            @Override
            protected void updateItem(hebergement hebergement, boolean empty) {
                super.updateItem(hebergement, empty);
                if (empty || hebergement == null) {
                    setText(null);
                } else {
                    setText(hebergement.getNom() + " - " +
                            hebergement.getDescription() + " - " +
                            hebergement.getPrix_nuit() + " TND - " +
                            hebergement.getType() + " - " +
                            hebergement.getAdresse()); // Correction ici (concaténation)
                }
            }
        });
    }

    private void showhebergementDetails() {
        hebergement selectedhebergement = listview.getSelectionModel().getSelectedItem();
        if (selectedhebergement != null) {
            // Mettre à jour les labels avec les détails de l'hébergement sélectionné
            nomhebergement.setText(selectedhebergement.getNom());
            desc.setText(selectedhebergement.getDescription());
            type.setText(selectedhebergement.getType());
            priix.setText(String.valueOf(selectedhebergement.getPrix_nuit()) + " TND");
            adresse.setText(selectedhebergement.getAdresse());
        }
    }

    @FXML
    void updatehebergement(ActionEvent event) {
        hebergement selectedhebergement = listview.getSelectionModel().getSelectedItem();

        if (selectedhebergement != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Updatehebergement.fxml"));
                Parent root = loader.load();

                Updatehebergement updatehebergementController = loader.getController();
                updatehebergementController.sethebergementDetails(selectedhebergement); // Passer les données à la page de modification

                Scene currentScene = ((Node) event.getSource()).getScene();
                currentScene.setRoot(root);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("⚠ Veuillez sélectionner un hébergement à modifier.");
        }

    }

    @FXML
    void supprimer(ActionEvent event) {
        hebergement selectedhebergement = listview.getSelectionModel().getSelectedItem();

        if (selectedhebergement != null) {
            try {
                servicehebergement.delete(selectedhebergement.getId());

                // Mise à jour de la ListView après suppression
                listview.getItems().remove(selectedhebergement);
                listview.refresh();

                System.out.println("✅ Hébergement supprimé avec succès !");
            } catch (SQLException e) {
                System.out.println("❌ Erreur : Impossible de supprimer l'hébergement.");
                e.printStackTrace();
            }
        } else {
            System.out.println("⚠ Aucun hébergement sélectionné.");
        }
    }
}
