package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import tn.esprit.entities.reponse;
import tn.esprit.services.ServiceReponse;

import java.sql.SQLException;
import java.util.List;

public class ReponseController {

    @FXML
    private TableView<reponse> tableResponses;
    @FXML
    private TableColumn<reponse, Integer> colResponseId;
    @FXML
    private TableColumn<reponse, String> colResponseMessage;
    @FXML
    private TableColumn<reponse, String> colResponseDate;
    @FXML
    private TextArea textAreaResponse;

    private ServiceReponse serviceReponse = new ServiceReponse();
    int reclamationId = ReclamationController.id_reclamation;

    @FXML
    public void initialize() {
        loadResponses();
    }

    private void loadResponses() {
        try {
            List<reponse> responses = serviceReponse.getReponsesByReclamationId(reclamationId);
            colResponseId.setCellValueFactory(new PropertyValueFactory<>("id"));
            colResponseMessage.setCellValueFactory(new PropertyValueFactory<>("message"));
            colResponseDate.setCellValueFactory(new PropertyValueFactory<>("date_reponse"));
            tableResponses.getItems().setAll(responses);
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the error
        }
    }

    @FXML
    private void addResponse() {
        String message = textAreaResponse.getText();
        if (message != null && !message.isEmpty()) {
            reponse newReponse = new reponse();
            newReponse.setMessage(message);
            newReponse.setDate_reponse(new java.sql.Date(System.currentTimeMillis()));
            try {
                serviceReponse.ajouter(newReponse, reclamationId);
                loadResponses();  // Reload the responses after adding a new one
                textAreaResponse.clear(); // Clear the input field
            } catch (SQLException e) {
                e.printStackTrace(); // Handle the error
            }
        }
    }
}
