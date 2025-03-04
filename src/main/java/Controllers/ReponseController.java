package Controllers;

import Entities.Reclamation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.fxml.FXMLLoader;
import javafx.scene.text.Text;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import Entities.Reponse;
import Services.ReponseService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ReponseController {

    @FXML
    private ListView<Reponse> ReponseListView;

    @FXML
    private GridPane reponseContainer;

    @FXML
    private Button backButton;

    @FXML
    private TextField searchField;

    private final ReponseService reponseService = new ReponseService(); // Assuming you have a service class

    private ObservableList<Reponse> reponseList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        loadResponses(); // Load responses into ListView
        //setupListView(); // Align rows with headers
    }

    public void loadResponses() {
        //int currentUserId = 1; // 🔥 Hardcoded user ID for testing

        List<Reponse> reponses = reponseService.afficher();
        int column = 0;
        int row = 1;

        try {
            reponseContainer.getChildren().clear();

            reponseContainer.setHgap(10);
            reponseContainer.setVgap(10);
            reponseContainer.setPadding(new Insets(10));

            for (Reponse reponse : reponses) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/reponseCard.fxml"));
                Parent card = fxmlLoader.load();

                    CardReponseController cardController = fxmlLoader.getController();
                cardController.setData(reponse, this);

                reponseContainer.add(card, column, row);
                column++;
                if (column == 7) {
                    column = 0;
                    row++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleAddReponse(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/addReponse.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Ajouter Réponse");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            loadResponses();
        } catch (IOException e) {
            showAlert("Erreur", "Impossible d'ouvrir le formulaire d'ajout.");
            e.printStackTrace();
        }
    }


    @FXML
    void handleSearch(ActionEvent event) {
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            loadResponses(); // Reload all responses if search field is empty
            return;
        }

        try {
            List<Reponse> filteredReponses = reponseService.searchReponses(query);
            ObservableList<Reponse> observableList = FXCollections.observableArrayList(filteredReponses);
            ReponseListView.setItems(observableList);
        } catch (SQLException e) {
            showAlert("Erreur", "Impossible d'effectuer la recherche.");
            e.printStackTrace();
        }
    }




    @FXML
    void handleLogout(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Déconnexion");
        alert.setHeaderText("Voulez-vous vous déconnecter ?");
        if (alert.showAndWait().get() == ButtonType.OK) {
            ((Stage) backButton.getScene().getWindow()).close();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
