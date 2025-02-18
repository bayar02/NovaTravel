package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.hebergement;
import services.Servicehebergement;
import utils.MyDb;

import java.sql.Connection;
import java.sql.SQLException;

public class Updatehebergement {

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
    void modifier_photo(ActionEvent event) {

    }




private hebergement selectedhebergement;
Servicehebergement servicehebergement;
public Updatehebergement () {
    this.servicehebergement = new Servicehebergement();
}

public void sethebergementDetails(hebergement hebergement) {
    this.selectedhebergement = hebergement;

    nom_hebergement.setText(hebergement.getNom());
    description.setText(hebergement.getDescription());
    prix.setText(String.valueOf(hebergement.getPrix_nuit()));
    type.setText(String.valueOf(hebergement.getType()));
    adresse.setText(hebergement.getAdresse());
}

@FXML
void saveChanges(ActionEvent event) throws SQLException {
    Connection cnx = MyDb.getInstance().getConnection();

    try {
        if (cnx == null || cnx.isClosed()) {
            System.out.println("Reconnecting to the database...");
            cnx = MyDb.getInstance().getConnection();
        }
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
    // Update the selected pack with new data from the text fields
    selectedhebergement.setNom(nom_hebergement.getText());
    selectedhebergement.setDescription(description.getText());
    selectedhebergement.setPrix_nuit(Float.parseFloat(prix.getText()));
    selectedhebergement.setType(type.getText());
    selectedhebergement.setAdresse(adresse.getText());

    try {
        servicehebergement.update(selectedhebergement);
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }

    System.out.println(" Hebergement updated successfully!");



}}

//kkkkkkkkkkkk