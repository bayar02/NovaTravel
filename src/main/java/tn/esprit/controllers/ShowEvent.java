package tn.esprit.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

import javafx.stage.Stage;
import tn.esprit.entities.Event;
import tn.esprit.service.ServiceEvent;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ShowEvent  {

    @FXML
        private ListView<Event> listEvent;

    private Event selectedEvent;
    private ObservableList<Event> eventList = FXCollections.observableArrayList();



    @FXML
    private final ServiceEvent se = new ServiceEvent();


    @FXML
    public void initialize() throws SQLException {
        loadEvents();
    }
    private void loadEvents() throws SQLException {
        eventList.clear();
        List<Event> events = se.afficher();
        eventList.addAll(events);
        listEvent.setItems(eventList);

        // Custom cell factory to format each row
        listEvent.setCellFactory(lv -> new ListCell<Event>() {
            @Override
            protected void updateItem(Event event, boolean empty) {
                super.updateItem(event, empty);
                if (empty || event == null) {
                    setText(null);
                } else {
                    setText(String.format("%-20s %-30s %-15s %-15s %-10s H %-10s TND",
                            event.getNom(),
                            event.getDescription(),
                            event.getLieu(),
                            event.getDateEvent(),
                            event.getDuree(),
                            event.getPrix()));
                }
            }
        });
    }



    public void supprimerEvent(ActionEvent actionEvent) throws SQLException {
        if (selectedEvent == null) {
            showAlert("Veuillez sélectionner un événement à supprimer.", Alert.AlertType.WARNING);
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText("Voulez-vous vraiment supprimer cet événement ?");
        Optional<ButtonType> result = confirmation.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            se.supprimer(selectedEvent.getId());
            showAlert("Événement supprimé avec succès.", Alert.AlertType.INFORMATION);
            loadEvents();
        }
    }

    private void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void handleClear(ActionEvent actionEvent) {
        selectedEvent = null;
        listEvent.getSelectionModel().clearSelection();
    }

    public void handleEventSelection(javafx.scene.input.MouseEvent mouseEvent)throws SQLException {
        int index = listEvent.getSelectionModel().getSelectedIndex();
        if (index >= 0) {
            List<Event> events = se.afficher();
            selectedEvent = events.get(index);
        }
    }

    public void goToUpdatePage(ActionEvent actionEvent) {

        try {
            if (selectedEvent != null) {
                // Load the UpdateEvent FXML
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateEvent.fxml"));
                Parent root = loader.load();

                // Get the controller of the UpdateEvent page
                updateEvent updateController = loader.getController();

                // Pass the selected event data to the update page
                updateController.preloadEventData(selectedEvent);

                // Display the UpdateEvent page
                Scene scene = new Scene(root);
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } else {
                System.out.println("No event selected!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}


