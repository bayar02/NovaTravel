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
import tn.esprit.entities.Planning;
import tn.esprit.entities.Event;
import tn.esprit.service.ServicePlanning;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ShowPlanning implements Initializable {

    @FXML
    private ListView<Planning> listPlanning;

    private Planning selectedPlanning;
    private ObservableList<Planning> planningList = FXCollections.observableArrayList();

    private final ServicePlanning servicePlanning = new ServicePlanning();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            loadPlannings();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadPlannings() throws SQLException {
        planningList.clear();
        List<Planning> plannings = servicePlanning.afficher();
        planningList.addAll(plannings);
        listPlanning.setItems(planningList);

        listPlanning.setCellFactory(lv -> new ListCell<Planning>() {
            @Override
            protected void updateItem(Planning planning, boolean empty) {
                super.updateItem(planning, empty);
                if (empty || planning == null) {
                    setText(null);
                } else {
                    StringBuilder eventNames = new StringBuilder();
                    for (Event event : planning.getEvents()) {
                        eventNames.append(event.getNom()).append(", ");
                    }
                    if (eventNames.length() > 0) {
                        eventNames.setLength(eventNames.length() - 2);
                    }

                    setText(String.format("Date: %s\nNom: %s\nÉvénements: %s",
                            planning.getDateCreation(),
                            planning.getNom(),
                            eventNames.toString()));

                }
            }
        });
    }

    public void supprimerPlanning(ActionEvent actionEvent) throws SQLException {
        if (selectedPlanning == null) {
            showAlert("Veuillez sélectionner un planning à supprimer.", Alert.AlertType.WARNING);
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText("Voulez-vous vraiment supprimer ce planning ?");
        Optional<ButtonType> result = confirmation.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            servicePlanning.supprimer(selectedPlanning.getId());
            showAlert("Planning supprimé avec succès.", Alert.AlertType.INFORMATION);
            loadPlannings();
        }
    }

    private void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void handleClear(ActionEvent actionEvent) {
        selectedPlanning = null;
        listPlanning.getSelectionModel().clearSelection();
    }

    public void handlePlanningSelection(javafx.scene.input.MouseEvent mouseEvent) {
        selectedPlanning = listPlanning.getSelectionModel().getSelectedItem();
    }




    public void goToUpdatePlanningPage(ActionEvent actionEvent) {

        try {
            if (selectedPlanning != null) {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/updatePlanning.fxml"));
                Parent root = loader.load();

                updatePlanning updateController = loader.getController();

                // Pass the selected planning data to the update page
                updateController.preloadPlanningData(selectedPlanning);

                Scene scene = new Scene(root);
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } else {
                System.out.println("No planning selected!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
