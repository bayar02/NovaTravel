package Controllers;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.hebergement;
import services.Servicehebergement;

import java.io.File;
import java.io.IOException;





public class Addhebergement {
    Servicehebergement servicehebergement ;
    public Addhebergement() {
        this.servicehebergement = new Servicehebergement();
    }


    @FXML
    private TextField adresse;

    @FXML
    private TextField description;

    @FXML
    private TextField nom_hebergement;

    @FXML
    private TextField prix;

    @FXML
    private TextField type;

    @FXML
    void ajouter_photo(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select an Image");

        // Set file filter to show only image files
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            String imagePath = selectedFile.toURI().toString(); // Save path
            System.out.println("Image selected: " + imagePath);
        } else {
            System.out.println("No image selected.");
        }

    }




    @FXML
    void showhebergement(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Showhebergement.fxml"));
            Parent root = loader.load();

            // Afficher la nouvelle fenêtre
            Scene currentScene = ((Node) event.getSource()).getScene();
            currentScene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void save(ActionEvent event) {
        String nom = nom_hebergement.getText();
        System.out.println(nom);
        if ( nom.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Le nom de l'hébergement ne peut pas être vide.");
            alert.showAndWait();
            return;
        }

        String descriptionText = description.getText();
        String prixText = prix.getText();
        String typeText = type.getText();
        String adresseText = adresse.getText();

        hebergement hebergement = new hebergement(nom, typeText, adresseText,descriptionText, Float.parseFloat(prixText));
        try {
            servicehebergement.create(hebergement);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setContentText("Hébergement créé avec succès !");
            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Erreur : " + e.getMessage());
            alert.showAndWait();
        }
    }




}

