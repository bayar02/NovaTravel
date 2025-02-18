package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import tn.esprit.models.Pack;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ShowPackController implements Initializable {
    @FXML
    private TableView<Pack> packTable;
    @FXML
    private TableColumn<Pack, String> nomPackCol;
    @FXML
    private TableColumn<Pack, String> descriptionCol;
    @FXML
    private TableColumn<Pack, Double> prixCol;
    @FXML
    private TableColumn<Pack, String> dureeCol;
    @FXML
    private TableColumn<Pack, String> avantagesCol;
    @FXML
    private TableColumn<Pack, String> statutCol;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        nomPackCol.setCellValueFactory(new PropertyValueFactory<>("nomPack"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        prixCol.setCellValueFactory(new PropertyValueFactory<>("prix"));
        dureeCol.setCellValueFactory(new PropertyValueFactory<>("duree"));
        avantagesCol.setCellValueFactory(new PropertyValueFactory<>("avantages"));
        statutCol.setCellValueFactory(new PropertyValueFactory<>("statut"));
        
        // Load data from database
        loadPackData();
    }

    private void loadPackData() {
        // TODO: Implement database loading logic
    }

    @FXML
    private void goBack(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AddPack.fxml"));
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
} 